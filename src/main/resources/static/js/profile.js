document.addEventListener('DOMContentLoaded', function() {
    // Проверяем наличие JWT токена в cookie
    const jwtToken = getCookieJwt('jwt');
    getProfileData(jwtToken);

    // Получаем элементы для кнопок
    document.getElementById('logout').addEventListener('click', function() {
        if (jwtToken) {
            if(confirm("Вы уверены, что хотите выйти из аккаунта?"))
            {
                setCookie('jwt',null,0);
                window.location.href = window.location.href.replace('/profile','/auth');
            }
        } 
        else 
        {
            alert("Невозможно выйти: Авторизация не выполнена!");
        }
    })
});

// Функция для получения cookie по имени
function getCookieJwt(name) {
    const cookieArr = document.cookie.split(';');
    for (let i = 0; i < cookieArr.length; i++) {
        const cookiePair = cookieArr[i].split('=');
        if (name === cookiePair[0].trim()) {
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}
async function getProfileData(jwt){
    try {
        const response = await fetch(`http://localhost:8080/api/users/data?jwt=${jwt}`);
        const data = await response.json();

        // Отображаем данные о медикаменте на странице
        document.getElementById('email').value = data.email;
        document.getElementById('name').value = `${data.name}`;
        document.getElementById('address').value = `${data.address}`;
    } catch (error) {
        console.error('Ошибка при получении данных о медикаменте:', error);
    }
}
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = name + '=' + encodeURIComponent(value) + ';expires=' + expires.toUTCString();
}