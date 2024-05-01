const urlParams = new URLSearchParams(window.location.search);
    const _page = urlParams.get('page') || 0; // Если параметр не указан, используется значение по умолчанию 1
    const _search = urlParams.get('search') || '';
    const pageSize = 16; // Размер страницы

    // Функция для загрузки данных о пользователях
    function loadAids(page) {
        const url = `http://localhost:8080/api/aids?page=${page}&size=${pageSize}&search=${_search}`;
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
                    const aidCard = document.createElement('div');
                    aidCard.classList.add('medicine');

                    // Заполнение информации о пользователе
                    aidCard.innerHTML = `
                        <a href="api/aids/${aid.id}">
                        <img src="${aid.imageURL}" alt="Картинка">
                        </a>
				        <h3 class="medicine-name">${aid.name}</h3>
				        <p class="medicine-description">${aid.manufacturer}</p><br/>
                        <p class="price">${aid.price} ₽</p>
				        <button class = "medicine-buy" type="button">В корзину</button>
                    `;

                    // Добавление карточки в контейнер
                    aidsContainer.appendChild(aidCard);
                });
            })
            .catch(error => console.error('Ошибка загрузки товаров:', error));
    }
    function loadPages() {
        // Запрос на получение данных о пользователях
        const url2 = `http://localhost:8080/api/aids/count?search=${_search}`;
        fetch(url2)
        .then(response => response.json())
        .then(data => {
            const numberData = Number(data); // Преобразование данных в число
            const container = document.getElementById('pages-container');

            if(numberData > pageSize){
            // Цикл для создания ссылок на страницы
            for (let page = 0; page < numberData/pageSize; page++) {
                        const link = document.createElement('a');
                        link.href = `?page=${page}&search=${_search}`;
                        link.textContent = `${page+1}`;
                        link.className = 'pageLink';
                        if(page ==_page)
                            link.style = 'background-color: #054e23;';

                        // Добавление ссылки в контейнер
                        container.appendChild(link);
                    }
                }
            })
            .catch(error => console.error('Ошибка:', error));
        }
    // Функция для перехода к профилю выбранного пользователя
    function viewAid(aidId) {
        window.location.href = `http://localhost:8080/api/aids/${aidId}`;
    }
    function search(){
        const search_val = document.getElementById("search_input").value;
        window.location.href = `http://localhost:8080/catalog?search=${search_val}`;
    }
    document.addEventListener("DOMContentLoaded", function() {
            const searchInput = document.getElementById("search_input");
            loadAids(_page);
            loadPages();
            if (searchInput && _search) {
                searchInput.value = _search;
            }
        });