
// Funkcja do pobierania danych strumienia z Twitch API
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
        return data.data[0]; // Zwraca dane pierwszego strumienia (jeśli istnieje)
    } catch (error) {
        console.log('Wystąpił błąd:', error);
    }
}

// Funkcja do osadzania strumienia na stronie
function embedStream(streamData) {
    if (streamData) {
        const channelName = streamData.user_name; // Pobiera nazwę kanału z danych strumienia
        const parent = 'localhost';
        console.log(parent)
        const streamEmbedCode = `<iframe src="https://player.twitch.tv/?channel=${channelName}&parent=${parent}" frameborder="0" allowfullscreen="true" scrolling="no" height="378" width="620"></iframe>
`;
        document.getElementById('stream-container').innerHTML = streamEmbedCode;
    } else {
        document.getElementById('stream-container').innerHTML = 'Obecnie nie ma aktywnego strumienia.';
    }
}

// Wywołanie funkcji i osadzenie strumienia na stronie
getStreamData().then(streamData => {
    embedStream(streamData);
});
