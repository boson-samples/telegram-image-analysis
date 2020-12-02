package function

import (
	"context"
	"fmt"
	"os"

	cloudevents "github.com/cloudevents/sdk-go/v2"
)

type Message struct {
	Photos []interface{} `json:"photo"`
}

// Handle a CloudEvent.
func Handle(ctx context.Context, event cloudevents.Event) (resp *cloudevents.Event, err error) {
	if err = event.Validate(); err != nil {
		fmt.Fprintf(os.Stderr, "invalid event received. %v", err)
		return
	}

	fmt.Printf("%v\n", event)

	msg := &Message{}
	event.DataAs(msg)
	if err = event.DataAs(msg); err != nil {
		fmt.Printf("failed to parse Telegram message %s\n", err.Error())
		return
	}

	if len(msg.Photos) == 0 {
		//doesn't contain a photo -> do nothing
		fmt.Println("received Telegram message without a photo.")
		return
	}

	fmt.Println("received Telegram message with a photo.")
	// send a CloudEvent with photos
	response := cloudevents.NewEvent()
	response.SetID(event.ID())
	response.SetSource("function:receiver")
	response.SetType("image:received")
	response.DataEncoded = event.Data()

	resp = &response

	if err = resp.Validate(); err != nil {
		fmt.Printf("invalid event created. %v", err)
	}

	return
}
