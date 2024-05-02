// Получаем id из адреса страницы
const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');

// Функция для отправки запроса к API и отображения данных о медикаменте
async function getAidDetails() {
    try {
        const response = await fetch(`http://localhost:8080/api/aids/${id}`);
        const data = await response.json();

        // Отображаем данные о медикаменте на странице
        document.getElementById('aid_image').src = data.imageURL;
        document.getElementById('medicine-name').textContent = data.name;
        document.getElementById('manufacturer').textContent = `${data.manufacturer}`;
        document.getElementById('description').textContent = `${data.description}`;
        if(data.discountPercent != 0)
            document.getElementById('old_price').textContent = `${data.price} руб.\t`;
        document.getElementById('price').textContent = `${data.price * (1-(data.discountPercent / 100))} руб.`;
    } catch (error) {
        console.error('Ошибка при получении данных о медикаменте:', error);
    }
}

// Вызываем функцию для получения данных о медикаменте
getAidDetails();
