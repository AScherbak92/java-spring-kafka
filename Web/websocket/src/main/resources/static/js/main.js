function connect() {
    var socket = new SockJS(websocketUrl);
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(topic, function (message) {
            showOrderEvent(JSON.parse(message.body));
        });
    });
}

function showOrderEvent(orderEvent) {
    const orderElement = document.createElement('div');
    orderElement.className = 'order-event';

    const timestamp = orderEvent.orderTimestamp ?
        new Date(orderEvent.orderTimestamp).toLocaleString() :
        new Date().toLocaleString();

    orderElement.innerHTML = `
        <div class="order-header">
            <span class="order-id"> Order #${orderEvent.id || 'N/A'}</span>
            <span class="order-price"> $${orderEvent.price || 0}</span>
        </div>
        <div class="order-description">${orderEvent.description || 'No description'}</div>
        <div class="order-timestamp"> ${timestamp}</div>
    `;

    const messagesContainer = document.getElementById('messages');
    const systemMessage = messagesContainer.querySelector('.system-message');
    if (systemMessage) {
        systemMessage.remove();
    }
    messagesContainer.insertBefore(orderElement, messagesContainer.firstChild);
}

//Initialization
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("connect").addEventListener("click", connect);
});