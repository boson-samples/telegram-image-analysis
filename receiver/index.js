'use strict';
const axios = require('axios');
const { CloudEvent, HTTP } = require('cloudevents');
const API_KEY = process.env.API_KEY || 'dcc086604d47489e86bd6d216aa5b09b';

// Receives a CloudEvent with a Telegram Message
// https://core.telegram.org/bots/api#message
function receive(context, m) {
  console.log(context.cloudevent);
  console.log(m)
  const message = context;
  // Don't reply with an error, but we've got no action
  // to take if there is no message
  if (!message) {
    context.log.warn(`No message received with context: ${context}`);
    return {
      message: 'No message received'
    };
  }
  context.log.info('Message: ', message);
  return {
    message: "OK"
  }

  // Get the last image in the photo array
  // const photo = message.photo[message.photo.length-1];
  // axios.post({
  //     headers,
  //     url,
  //     params,
  //     data: {
  //       url: photo.fileLink
  //     }
  //   }).then(response => {
  //     context.log.info(response);

  //     return HTTP.binary(new CloudEvent({
  //       source: 'function.processor',
  //       type: 'image:analyzed',
  //       data: response
  //     }));
  //   }).catch(err => {
  //   return {
  //     statusCode: 500,
  //     body: err.message
  //   }
  // });
};

module.exports = receive;
