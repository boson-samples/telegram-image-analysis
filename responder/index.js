'use strict';
const { CloudEvent, HTTP } = require('cloudevents');
const axios = require('axios');

const token = process.env.TELEGRAM_API_KEY;
const url = `https://api.telegram.org/bot${token}/sendMessage`;

// Sanity check - we can't do anything without an API token
if (!token) {
  throw new Error('No $TELEGRAM_API_KEY found.');
}

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
      chat_id: response[0].chat,
      text: formatResponse(response)
    })
    .then(_ => context.log.info('Done'))
    .catch(err => context.log.error(err));
  });

};

function formatResponse(response) {
  let faces = response.length === 1 ? 'face' : 'faces';
  let text = `Hi! Thanks for playing. :)
I found ${response.length} ${faces} in this image.
`;

  response.forEach(image => {
    text += `

* Age: ${image.age}`;

    for(const emotion in image.emotion) {
      text += `
  - ${emotion}: ${image.emotion[emotion]}`;
    }
  });
  return text;
}

module.exports = sendReply;
