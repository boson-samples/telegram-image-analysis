'use strict';
const axios = require('axios');
const { CloudEvent, HTTP } = require('cloudevents');

const API_KEY = process.env.API_KEY || 'dcc086604d47489e86bd6d216aa5b09b';
const url = 'https://boson.cognitiveservices.azure.com/face/v1.0/detect';
const params = {
  returnFaceId: true,
  returnFaceLandmarks: false,
  returnFaceAttributes: "age,gender,emotion",
  recognitionModel: "recognition_03",
  returnRecognitionModel: false,
  detectionModel: "detection_01"
}
const headers = {
  'Ocp-Apim-Subscription-Key': API_KEY
}

// Receives a CloudEvent with a Telegram Message
// https://core.telegram.org/bots/api#message
async function receive(context, message) {
  // Don't reply with an error, but we've got no action
  // to take if there is no message
  if (!message) {
    context.log.warn(`No message received with context: ${context}`);
    return {
      message: 'No message received'
    };
  }
  context.log.info('Message: ', message);

  // Get the last image in the photo array
  try {
    const photo = message.photo[message.photo.length-1];
    const response = await axios.post({
      headers,
      url,
      params,
      data: {
        url: photo.fileLink
      }
    });
    context.log.info(response);

    return HTTP.binary(new CloudEvent({
      source: 'function.processor',
      type: 'image:analyzed',
      data: response
    }));
  } catch (err) {
    return {
      statusCode: 500,
      body: err.message
    }
  }
};

module.exports = receive;
