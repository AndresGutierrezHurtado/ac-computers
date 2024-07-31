document.addEventListener('DOMContentLoaded', function () {
    // Inicializar el Swiper para la sección de inicio
    var homeSwiper = new Swiper('.home-swiper', {
        slidesPerView: 1,
        loop: false,
        navigation: {
            nextEl: ".swiper-button-next-home",
            prevEl: ".swiper-button-prev-home",
        },
        pagination: {
            el: ".swiper-pagination-home",
            clickable: true
        },
        autoplay: {
            delay: 9000,
            disableOnInteraction: false,
        },
    });
    // Inicializar el Swiper para la sección de inicio
    var newSwiper = new Swiper('.new-swiper', {
        slidesPerView: 3,
        spaceBetween: 120,
        loop: false,
        navigation: {
            nextEl: ".swiper-button-next-new",
            prevEl: ".swiper-button-prev-new",
        }
    });
});