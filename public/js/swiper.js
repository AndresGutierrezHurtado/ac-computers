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

    let slides = 1;
    if (window.innerWidth < 768) {
        slides = 1
    } else if (window.innerWidth < 1024) {
        slides = 2
    } else {
        slides = 3
    }
    // Inicializar el Swiper para la sección de inicio
    var newSwiper = new Swiper('.new-swiper', {
        slidesPerView: slides,
        spaceBetween: 120,
        loop: false,
        navigation: {
            nextEl: ".swiper-button-next-new",
            prevEl: ".swiper-button-prev-new",
        }
    });
});