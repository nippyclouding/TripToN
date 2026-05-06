document.addEventListener('DOMContentLoaded', function() {
    const video = document.getElementById('background-video');
    const dream1Video = document.getElementById('dream1-video');
    const gradientOverlay = document.querySelector('.gradient-overlay');
    const textImage = document.querySelector('.text-image');
    const text1Image = document.querySelector('.text1-image');
    const text2Image = document.querySelector('.text2-image');
    const text3Image = document.querySelector('.text3-image');
    const text4Image = document.querySelector('.text4-image');
    const text5Image = document.querySelector('.text5-image');
    const logoImage = document.querySelector('.logo-image');
    const itemGif = document.getElementById('item-gif');
    const arrowButton = document.querySelector('.arrow-button');
    const webmItems = document.querySelectorAll('.webm-item');

    function ensureVideoPlay(videoElement) {
        videoElement.play().catch(function(error) {
            console.log('Auto-play was prevented:', error);
            document.addEventListener('click', function() {
                videoElement.play();
            }, { once: true });
        });
    }

    ensureVideoPlay(video);
    ensureVideoPlay(dream1Video);

    webmItems.forEach(function(item) {
        const itemVideo = item.querySelector('.webm-video');
        const itemNumber = item.getAttribute('data-item');
        const popup = document.getElementById('item' + itemNumber + '-popup');
        let isPlaying = false;

        item.addEventListener('click', function() {
            if (!isPlaying && itemVideo.paused) {
                isPlaying = true;
                item.classList.add('playing');
                itemVideo.play();

                // named function으로 등록해 ended 시 정확히 제거
                function onTimeUpdate() {
                    const timeLeft = itemVideo.duration - itemVideo.currentTime;
                    if (timeLeft <= 0.5 && timeLeft > 0 && !popup.classList.contains('show')) {
                        popup.classList.remove('hide');
                        popup.classList.add('show');
                    }
                }
                itemVideo.addEventListener('timeupdate', onTimeUpdate);

                itemVideo.addEventListener('ended', function() {
                    itemVideo.removeEventListener('timeupdate', onTimeUpdate);
                    setTimeout(function() {
                        hidePopup(popup);

                        item.style.transition = 'opacity 0.3s ease-in-out';
                        item.style.opacity = '0';

                        setTimeout(function() {
                            isPlaying = false;
                            item.classList.remove('playing');
                            itemVideo.currentTime = 0;

                            item.style.opacity = '1';

                            setTimeout(function() {
                                item.style.transition = '';
                            }, 300);
                        }, 300);
                    }, 1000);
                }, { once: true });
            }
        });
    });

    let gifPlaying = false;
    itemGif.addEventListener('click', function() {
        if (!gifPlaying) {
            const gifSrc = itemGif.src;
            itemGif.src = '';
            itemGif.src = gifSrc;
            gifPlaying = true;

            setTimeout(function() {
                gifPlaying = false;
            }, 3000);
        }
    });

    function hidePopup(popup) {
        popup.classList.remove('show');
        popup.classList.add('hide');
        setTimeout(function() {
            popup.classList.remove('hide');
        }, 300);
    }

    function isElementInViewport(element, threshold = 0.3) {
        if (!element) return false;
        const rect = element.getBoundingClientRect();
        const windowHeight = window.innerHeight;

        return (
            rect.top < windowHeight * (1 - threshold) &&
            rect.bottom > windowHeight * threshold
        );
    }

    function handleScroll() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        const windowHeight = window.innerHeight;

        const scrollProgress = Math.min(scrollTop / windowHeight, 1);

        if (scrollProgress > 0.02) {
            gradientOverlay.classList.add('visible');
        } else {
            gradientOverlay.classList.remove('visible');
        }

        const elements = [
            textImage, text1Image, text2Image, text3Image, text4Image,
            text5Image, logoImage, itemGif, arrowButton
        ];

        elements.forEach(function(element) {
            if (element && isElementInViewport(element)) {
                if (!element.classList.contains('visible')) {
                    element.classList.add('visible');
                }
            } else if (element) {
                element.classList.remove('visible');
            }
        });

        webmItems.forEach(function(item) {
            if (isElementInViewport(item)) {
                if (!item.classList.contains('visible')) {
                    item.classList.add('visible');
                    const itemVideo = item.querySelector('.webm-video');
                    if (itemVideo.ended) {
                        itemVideo.currentTime = 0;
                    }
                }
            } else {
                item.classList.remove('visible');
            }
        });

        if (itemGif && !isElementInViewport(itemGif)) {
            gifPlaying = false;
        }
    }

    // rAF으로 스크롤 이벤트 쓰로틀 (매 프레임 한 번만 실행)
    let scrollPending = false;
    window.addEventListener('scroll', function() {
        if (scrollPending) return;
        scrollPending = true;
        requestAnimationFrame(function() {
            scrollPending = false;
            handleScroll();
        });
    });

    setTimeout(handleScroll, 100);
});
