function loadCart(jwt) {
    const url = `http://localhost:8080/api/cart?jwt=${jwt}`;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка HTTP: ' + response.status);
            }
            return response.json(); // Преобразование ответа в JSON
        })
        .then(cartItems => {
            const aidsContainer = document.getElementById('aids-container');
            // Очистка контейнера перед добавлением новых товаров
            aidsContainer.innerHTML = '';

            // Проход по каждому товару в корзине и создание карточки
            cartItems.forEach(cartItem => {
                fetch(`http://localhost:8080/api/aids/${cartItem.aid_id}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Ошибка HTTP: ' + response.status);
                        }
                        return response.json(); // Преобразование ответа в JSON
                    })
                    .then(aid => {
                        const aidCard = document.createElement('div');
                        aidCard.classList.add('medicine');
                        aidCard.id = `item-${aid.id}`;

                        // Заполнение информации о товаре
                        const price = aid.discountPercent === 0 ? aid.price : (aid.price * (1 - (aid.discountPercent / 100)));
                        aidCard.innerHTML = `
                            <a href="aids?id=${aid.id}" style="text-decoration: none; color: #000;">
                                <img src="${aid.imageURL}" alt="Картинка">
                                <h3 class="medicine-name">${aid.name}</h3>
                            </a>
                            <a href="catalog?search=${aid.manufacturer}" style="text-decoration: none; color: #000;">
                                <p class="medicine-description">${aid.manufacturer}</p><br/>
                            </a>
                            <div class="cart-item-controls">
                                <input type="number" value="${cartItem.quantity}" min="1" id="quantity-${aid.id}">
                            </div>
                            <button class="medicine-buy" id="buy-${aid.id}" type="button">${price * cartItem.quantity} ₽</button>
                        `;

                        // Добавление карточки товара в контейнер
                        aidsContainer.appendChild(aidCard);
                        var docc = document.getElementById(`quantity-${aid.id}`);
                        docc.onblur = function() { 
                            if(docc.value < 1) docc.value = 1;
                            updateCartItem(aid.id,jwt,price);
                        }
                        docc.onchange = function() { 
                            if(docc.value < 1) docc.value = 1;
                            updateCartItem(aid.id,jwt,price);
                        }
                    })
                    .catch(error => console.error('Ошибка загрузки товара:', error));
            });
        })
        .catch(error => console.error('Ошибка загрузки корзины:', error));
}

function getCookie(name) {
    const cookieArr = document.cookie.split(';');
    for (let i = 0; i < cookieArr.length; i++) {
        const cookiePair = cookieArr[i].split('=');
        if (name === cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}

function updateCartItem(itemId, jwt, price) {
    const quantityInput = document.getElementById(`quantity-${itemId}`);
    const newQuantity = quantityInput.value;
    var cartItem = {
        user_id:null,
        aid_id: itemId,
        quantity: newQuantity
    };
    
    // Отправляем POST запрос на сервер
    fetch(`http://localhost:8080/api/cart/reset?jwt=${getCookie("jwt")}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cartItem)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Ошибка сервера: ' + response.status);
        }
        document.getElementById(`buy-${itemId}`).textContent = `${price*newQuantity} ₽`;
    })
    .catch(error => {
        alert('Ошибка при изменении количества: ' + error.message);
    });
}

loadCart(getCookie('jwt'));