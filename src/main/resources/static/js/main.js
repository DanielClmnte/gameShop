/* ============================================
   GameShop — main.js
   Mobile menu, Image fallback, Scroll animations
   ============================================ */

document.addEventListener('DOMContentLoaded', function () {

    /* ── Mobile hamburger menu ── */
    var toggle = document.getElementById('menuToggle');
    var nav = document.getElementById('navMain');
    if (toggle && nav) {
        toggle.addEventListener('click', function () {
            nav.classList.toggle('open');
            toggle.textContent = nav.classList.contains('open') ? '✕' : '☰';
        });
    }

    /* ── Image fallback: show placeholder emoji on error ── */
    document.querySelectorAll('.game-card-img img, .product-hero-img img, .cart-item-img img').forEach(function (img) {
        img.addEventListener('error', function () {
            this.style.display = 'none';
            var fallback = this.nextElementSibling;
            if (fallback && fallback.classList.contains('img-fallback')) {
                fallback.style.display = 'flex';
            }
        });
    });

    /* ── Scroll reveal: animate cards on scroll ── */
    if ('IntersectionObserver' in window) {
        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.08, rootMargin: '0px 0px -40px 0px' });

        document.querySelectorAll('.game-card, .feature-item, .stat-card, .pedido-card, .order-card').forEach(function (el, i) {
            el.style.opacity = '0';
            el.style.transform = 'translateY(20px)';
            el.style.transition = 'opacity .5s ease ' + (i % 6) * 0.06 + 's, transform .5s ease ' + (i % 6) * 0.06 + 's';
            observer.observe(el);
        });
    }

    /* ── Active nav link highlighting ── */
    var currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(function (link) {
        var href = link.getAttribute('href');
        if (href && href !== '/' && currentPath.startsWith(href)) {
            link.style.color = 'var(--text)';
        }
    });
});

