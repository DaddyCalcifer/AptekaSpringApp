const urlParams = new URLSearchParams(window.location.search);
    const _page = urlParams.get('page') || 0; // Если параметр не указан, используется значение по умолчанию 1
    const pageSize = 5; // Размер страницы

    // Функция для загрузки данных о пользователях
    function loadAids(page) {
        const url = `http://localhost:8080/api/aids?page=${page}&size=${pageSize}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка HTTP: ' + response.status);
                }
                return response.json(); // Преобразование ответа в JSON
            })
            .then(aids => {
                const aidsContainer = document.getElementById('aids-container');
                // Очистка контейнера перед добавлением новых пользователей
                aidsContainer.innerHTML = '';
                // Проход по каждому пользователю и создание карточки
                aids.forEach(aid => {
                    alert(aid.name + ' ' + aid.description + ' ' + aid.imageURL);
                    const aidCard = document.createElement('div');
                    aidCard.classList.add('medicine');

                    // Заполнение информации о пользователе
                    aidCard.innerHTML = `
                        <a href="api/aids/${aid.id}">
                        <img src="${aid.imageURL}" alt="Картинка">
                        </a>
				        <h3 class="medicine-name">${aid.name}</h3>
				        <p>${aid.manufacturer}</p><br/>
                        <p class="price">${aid.price} ₽</p>
				        <button class = "medicine-buy" type="button">В корзину</button>
                    `;

                    // Добавление карточки в контейнер
                    aidsContainer.appendChild(aidCard);
                });
            })
            .catch(error => console.error('Ошибка загрузки товаров:', error));
    }
    // Функция для перехода к профилю выбранного пользователя
    function viewAid(aidId) {
        window.location.href = `http://localhost:8080/api/aids/${aidId}`;
    }

    loadAids(_page);