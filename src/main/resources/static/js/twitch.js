
// Getting stream from Twitch API
const gameId = document.getElementById('twitchId').innerText;
console.log(gameId);
async function getStreamData() {
    try {
        const response = await fetch(`https://api.twitch.tv/helix/streams?game_id=${gameId}&first=1`, {
            headers: {
                'Authorization': 'Bearer ',
                'Client-Id': ''
            }
        });
        const data = await response.json();
        return data.data[0]; // Returns data of first stream
    } catch (error) {
        console.log('Error::', error);
    }
}

// Setting stream
function embedStream(streamData) {
    if (streamData) {
        const channelName = streamData.user_name; // Getting stream name from stream data
        const parent = 'localhost';
        console.log(parent)
        const streamEmbedCode = `<iframe src="https://player.twitch.tv/?channel=${channelName}&parent=${parent}" frameborder="0" allowfullscreen="true" scrolling="no" height="378" width="620"></iframe>
`;
        document.getElementById('stream-container').innerHTML = streamEmbedCode;
    } else {
        document.getElementById('stream-container').innerHTML = 'Stream is not available';
    }
}

// Calling the function and embedding the stream on the page
getStreamData().then(streamData => {
    embedStream(streamData);
});
