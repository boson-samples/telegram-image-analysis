package function

import (
	"context"
	"encoding/json"
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
}

type Response struct {
	Chat string `json:"chat"`
	Url  string `json:"url"`
}

type GetUrlResult struct {
	OK     string                 `json:"ok"`
	Result map[string]interface{} `json:"result"`
}

// Handle a CloudEvent.
func Handle(ctx context.Context, event cloudevents.Event) (resp *cloudevents.Event, err error) {
	if token == "" {
		// With no API token we can't do anything
		fmt.Fprint(os.Stderr, "no TELEGRAM_API_KEY found\n")
		return
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

	if len(msg.Photos) == 0 {
		//doesn't contain a photo -> do nothing
		fmt.Println("received Telegram message without a photo.")
		return
	}

	fmt.Println("received Telegram message with a photo.")

	// Get chat ID from Telegram message
	var chatId string
	if id, found := msg.Chat["id"]; found {
		chatId = id.(string)
	} else {
		fmt.Fprintf(os.Stderr, "failed to get chat_id from Telegram message\n")
		return
	}

	// Get ID of the last photo from Telegram message
	var fileId string
	if id, found := msg.Photos[len(msg.Photos)-1]["file_id"]; found {
		fileId = id.(string)
	} else {
		fmt.Fprintf(os.Stderr, "failed to get file_id from the last photo from Telegram message\n")
		return
	}

	var photoPath string
	photoPath, err = getPhotoURL(fileId)

	// send a CloudEvent with photos
	response := cloudevents.NewEvent()
	response.SetID(event.ID())
	response.SetSource("function:receiver")
	response.SetType("image:received")
	response.SetData(cloudevents.ApplicationJSON, Response{
		Chat: chatId,
		Url:  donwloadPhotoBaseUrl + photoPath,
	})

	resp = &response

	if err = resp.Validate(); err != nil {
		fmt.Printf("invalid event created. %v", err)
	}

	return
}

func getPhotoURL(fileId string) (string, error) {

	res, err := http.Post(
		getFileInfoUrl,
		"application/json; charset=UTF-8",
		strings.NewReader(fmt.Sprintf("{\"file_id\":\"%s\"}", fileId)),
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
		err := fmt.Errorf("failed to get file_path from Telegram message, data: %v\n", result)
		fmt.Fprintf(os.Stderr, err.Error())
		return "", err
	}

	return filepath, nil
}
