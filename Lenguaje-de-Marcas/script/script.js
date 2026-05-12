document.addEventListener('DOMContentLoaded', () => {
    const navItems = document.querySelectorAll('.nav__item');

    const activeItem = document.querySelector('.nav__item.active');
    if (activeItem) {
        const img = activeItem.querySelector('img');
        if (img) img.src = img.dataset.active;
    }

    navItems.forEach(item => {
        item.addEventListener('click', () => {

            navItems.forEach(i => {
                const img = i.querySelector('img');
                i.classList.remove('active');
                if (img) img.src = img.dataset.default;
            });

            item.classList.add('active');
            const activeImg = item.querySelector('img');
            if (activeImg) activeImg.src = activeImg.dataset.active;

            window.location.href = item.dataset.href;
        });
    });
});