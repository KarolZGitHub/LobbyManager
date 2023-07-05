// fetch('https://api.twitch.tv/helix/streams?game_id=21779&first=1&sort=viewers&order=desc', {
//     method: 'GET',
//     headers: {
//         'Authorization': 'Bearer 1deqvg24b4h5afy9sd4g6ak4letmxm',
//         'Client-Id': 'ig7356zk3ysiyo9k9ndcp08i5m4qnh'
//     }
// })
//     .then(response => response.json())
//     .then(data => {
//         // Check if there is a stream available
//         if (data.data && data.data.length > 0) {
//             const mostViewedStream = data.data[0];
//             const userId = mostViewedStream.user_id;
//             const streamUrl = 'https://api.twitch.tv/helix/videos?user_id=' + userId;
//             console.log(userId);
//             console.log(mostViewedStream);
//             console.log(streamUrl);
//
//             // Display the stream on the page
//             displayStream(streamUrl);
//         } else {
//             console.log('No streams available for the specified game.');
//         }
//     })
//     .catch(error => {
//         console.error('Failed to retrieve the most viewed stream:', error);
//     });
//
// // Function to display the stream
// function displayStream(url) {
//     const videoElement = document.createElement('video');
//     videoElement.src = url;
//     videoElement.controls = true;
//     videoElement.autoplay = true;
//     videoElement.style.width = '100%';
//     videoElement.style.height = 'auto';
//
//     document.body.appendChild(videoElement);
// }

// Funkcja do pobierania danych strumienia z Twitch API
async function getStreamData() {
    try {
        const response = await fetch(`https://api.twitch.tv/helix/streams?game_id=21779&first=1`, {
            headers: {
                'Authorization': 'Bearer 1deqvg24b4h5afy9sd4g6ak4letmxm',
                'Client-Id': 'ig7356zk3ysiyo9k9ndcp08i5m4qnh'
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
