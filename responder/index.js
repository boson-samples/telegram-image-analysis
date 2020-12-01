'use strict';
const { CloudEvent, HTTP } = require('cloudevents');
const axios = require('axios');

const token = process.env.API_TOKEN || "1332135856:AAHI8MlVWVlsZAIVsHGxeogWMSXEK0ETUpc";
const url = `https://api.telegram.org/bot${token}/sendMessage`;

// Should receive a cloud event with a Telegram chat ID
// Make this function async so we can return immediately
// to the invoker, while doing the work of replying to
// the original chat message.
async function sendReply(context, response) {
  if (!context.cloudevent) {
    return {
      message: 'No cloud event received'
    };
  }

  return new Promise((resolve, reject) => {
    // resolve immediately with HTTP 204 No Content
    resolve({ code: 204 });
    
    // send chat response async
    axios.post(url, {
      chat_id: response.chat,
      text: `Hi! Thanks for playing. ;) This person looks to be about ${response.age} and ${response.emotion}`
    })
    .then(_ => console.log('Done'))
    .catch(err => console.error(err));
  })

};

module.exports = sendReply;
