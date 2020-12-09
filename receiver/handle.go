package function

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"strings"

	cloudevents "github.com/cloudevents/sdk-go/v2"
)

var (
	token                = os.Getenv("TELEGRAM_API_KEY")
	getFileInfoUrl       = "https://api.telegram.org/bot" + token + "/getFile"
	donwloadPhotoBaseUrl = "https://api.telegram.org/file/bot" + token + "/"
)

type Message struct {
	Chat   map[string]interface{}   `json:"chat"`
	Photos []map[string]interface{} `json:"photo"`
	Text   string                   `json:"text"`
}

type Response struct {
	Chat string `json:"chat"`
	URL  string `json:"url"`
	Text string `json:"text"`
}

type GetUrlResult struct {
	OK     string                 `json:"ok"`
	Result map[string]interface{} `json:"result"`
}

// Handle a CloudEvent.
func Handle(ctx context.Context, event cloudevents.Event) (resp *cloudevents.Event, err error) {
	if token == "" {
		// With no API token we can't do anything
		return nil, errors.New("TELEGRAM_API_KEY environment variable not set")
	}

	if err = event.Validate(); err != nil {
		fmt.Fprintf(os.Stderr, "invalid event received. %v", err)
		return
	}

	msg := &Message{}
	event.DataAs(msg)
	if err = event.DataAs(msg); err != nil {
		fmt.Fprintf(os.Stderr, "failed to parse Telegram message %s\n", err)
		return
	}

	// Get chat ID from Telegram message
	var chatID string
	if id, found := msg.Chat["id"]; found {
		chatID = id.(string)
	} else {
		fmt.Fprintf(os.Stderr, "failed to get chat_id from Telegram message\n")
		return
	}

	if len(msg.Photos) == 0 {
		//doesn't contain a photo -> emit telegram.text event
		fmt.Println("received Telegram message without a photo.")
		// send a CloudEvent with photos
		response := cloudevents.NewEvent()
		response.SetID(event.ID())
		response.SetSource("function:receiver")
		response.SetType("telegram.text")
		response.SetData(cloudevents.ApplicationJSON, Response{
			Chat: chatID,
			Text: msg.Text,
		})

		resp = &response

		if err = resp.Validate(); err != nil {
			fmt.Printf("invalid event created. %v", err)
		}

		return
	}

	fmt.Println("received Telegram message with a photo.")

	// Get ID of the last photo from Telegram message
	var fileID string
	if id, found := msg.Photos[len(msg.Photos)-1]["file_id"]; found {
		fileID = id.(string)
	} else {
		fmt.Fprintf(os.Stderr, "failed to get file_id from the last photo from Telegram message\n")
		return
	}

	var photoPath string
	photoPath, err = getPhotoURL(fileID)

	// send a CloudEvent with photos
	response := cloudevents.NewEvent()
	response.SetID(event.ID())
	response.SetSource("function:receiver")
	response.SetType("telegram.image")
	response.SetData(cloudevents.ApplicationJSON, Response{
		Chat: chatID,
		URL:  donwloadPhotoBaseUrl + photoPath,
	})

	resp = &response

	if err = resp.Validate(); err != nil {
		fmt.Printf("invalid event created. %v", err)
	}

	return
}

func getPhotoURL(fileID string) (string, error) {

	res, err := http.Post(
		getFileInfoUrl,
		"application/json; charset=UTF-8",
		strings.NewReader(fmt.Sprintf("{\"file_id\":\"%s\"}", fileID)),
	)
	if err != nil {
		fmt.Fprintf(os.Stderr, "unable to get a photo URL. %v", err)
		return "", err
	}
	if res.Body != nil {
		defer res.Body.Close()
	}

	data, _ := ioutil.ReadAll(res.Body)
	result := GetUrlResult{}
	json.Unmarshal(data, &result)

	var filepath string
	if path, found := result.Result["file_path"]; found {
		filepath = path.(string)
	} else {
		err := fmt.Errorf("failed to get file_path from Telegram message, data: %v", result)
		fmt.Fprintf(os.Stderr, err.Error())
		return "", err
	}

	return filepath, nil
}
