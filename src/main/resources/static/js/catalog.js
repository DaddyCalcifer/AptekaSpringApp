const urlParams = new URLSearchParams(window.location.search);
    const _page = urlParams.get('page') || 0; // Если параметр не указан, используется значение по умолчанию 1
    const _search = urlParams.get('search') || '';
    const pageSize = 16; // Размер страницы
    const minP = urlParams.get('minP') || 0;
    const maxP = urlParams.get('maxP') || 999999;
    var sort = 'ASC';

    // Функция для загрузки данных о пользователях
    function loadAids(page) {
        const url = `http://localhost:8080/api/aids?page=${page}&size=${pageSize}&minP=${minP}&maxP=${maxP}&search=${_search}`;
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
                    if(aid.discountPercent==0)
                    aidCard.innerHTML = `
                        <a href="aids?id=${aid.id}" style="text-decoration: none; color: #000;">
                        <img src="${aid.imageURL}" alt="Картинка">
				        <h3 class="medicine-name">${aid.name}</h3>
                        </a>
                        <a href="catalog?search=${aid.manufacturer}" style="text-decoration: none; color: #000;">
				        <p class="medicine-description">${aid.manufacturer}</p><br/>
				        </a>
                        <p class="price">${aid.price} ₽</p>
				        <button class = "medicine-buy" type="button" onClick="addToCart(${aid.id});">В корзину</button>
                    `;
                    else
                    aidCard.innerHTML = `
                        <a href="aids?id=${aid.id}" style="text-decoration: none; color: #000;">
                        <img src="${aid.imageURL}" alt="Картинка">
				        <h3 class="medicine-name">${aid.name}</h3>
                        </a>
                        <a href="catalog?search=${aid.manufacturer}" style="text-decoration: none; color: #000;">
				        <p class="medicine-description">${aid.manufacturer}</p><br/>
				        </a>
                        <p class="price-old">${aid.price} ₽</p>
                        <p class="price-sale">${aid.price * (1-(aid.discountPercent / 100))} ₽</p>
				        <button class = "medicine-buy" type="button" onClick="addToCart(${aid.id});">В корзину</button>
                    `;
                    // Добавление карточки в контейнер
                    aidsContainer.appendChild(aidCard);
                });
            })
            .catch(error => console.error('Ошибка загрузки товаров:', error));
    }
    function loadSales() {
        const url = `http://localhost:8080/api/aids/sale`;
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Ошибка HTTP: ' + response.status);
                }
                return response.json(); // Преобразование ответа в JSON
            })
            .then(aids => {
                const salesContainer = document.getElementById('sale-container');
                // Очистка контейнера перед добавлением новых пользователей
                salesContainer.innerHTML = '';
                // Проход по каждому пользователю и создание карточки
                aids.forEach(aid => {
                    const aidCard = document.createElement('div');
                    aidCard.classList.add('medicine');

                    aidCard.innerHTML = `
                        <a href="aids?id=${aid.id}" style="text-decoration: none; color: #000;">
                        <img src="${aid.imageURL}" alt="Картинка">
				        <h3 class="medicine-name">${aid.name}</h3>
                        </a>
                        <a href="catalog?search=${aid.manufacturer}" style="text-decoration: none; color: #000;">
				        <p class="medicine-description">${aid.manufacturer}</p><br/>
				        </a>
                        <p class="price-old">${aid.price} ₽</p>
                        <p class="price-sale">${aid.price * (1-(aid.discountPercent / 100))} ₽</p>
				        <button class = "medicine-buy" type="button" onClick="addToCart(${aid.id});">В корзину</button>
                    `;
                    // Добавление карточки в контейнер
                    salesContainer.appendChild(aidCard);
                });
            })
            .catch(error => console.error('Ошибка загрузки товаров:', error));
    }
    function changeSort()
    {
            var button = document.getElementById('sort_button');

            if (sort === 'DESC') {
                sort = 'ASC';
                button.textContent = 'Сначала дешевые';
            } else {
                sort = 'DESC';
                button.textContent = 'Сначала дорогие';
            }
    }
    function loadPages() {
        // Запрос на получение данных о пользователях
        const url2 = `http://localhost:8080/api/aids/count?search=${_search}&minP=${minP}&maxP=${maxP}`;
        fetch(url2)
        .then(response => response.json())
        .then(data => {
            const numberData = Number(data); // Преобразование данных в число
            const container = document.getElementById('pages-container');

            if(numberData > pageSize){
            // Цикл для создания ссылок на страницы
            for (let page = 0; page < numberData/pageSize; page++) {
                        const link = document.createElement('a');
                        link.href = `?page=${page}&search=${_search}&minP=${minP}&maxP=${maxP}#aids-container`;
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
    function applyFilter(){
            const search_val = document.getElementById("search_input").value;
            const min_Price = document.getElementById("minPrice").value;
            const max_Price = document.getElementById("maxPrice").value;
            window.location.href = `http://localhost:8080/catalog?search=${search_val}&minP=${min_Price}&maxP=${max_Price}`;
    }

    function addToCart(aidId) {
        // Получаем значения полей формы
        
        // Формируем JSON объект для отправки на сервер
        var cartItem = {
            user_id:null,
            aid_id: aidId,
            quantity: 1
        };
        
        // Отправляем POST запрос на сервер
        fetch(`http://localhost:8080/api/cart/add?jwt=${getCookie("jwt")}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cartItem)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сервера: ' + response.status);
            }
            // Дополнительные действия после успешной регистрации
        })
        .catch(error => {
            alert('Ошибка при добавлении в корзину: ' + error.message);
        });
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
        document.addEventListener("DOMContentLoaded", function() {
            const searchInput = document.getElementById("search_input");
            const minPriceI = document.getElementById("minPrice");
            const maxPriceI = document.getElementById("maxPrice");
            loadAids(_page);
            loadSales();
            loadPages();
            if (searchInput && _search) {
                searchInput.value = _search;
            }
            if (minPriceI && minP) {
                minPriceI.value = minP;
            }
            if (maxPriceI && maxP && maxP < 999999) {
                maxPriceI.value = maxP;
            }
        });