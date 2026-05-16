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
    const webmSections = document.querySelectorAll('.webm1-section, .webm2-section, .webm3-section');
    const submarineSection = document.querySelector('.dream1-section');
    const completedAnimations = new Set();
    let lockedSection = null;
    let lockedSubmarineSection = null;
    let touchStartY = 0;
    let dragStartX = 0;
    let dragStartY = 0;
    let isSubmarineDragActive = false;
    let submarineAnimationCompleted = false;
    let submarineAnimationPlaying = false;
    let submarineUnlockTimer = null;

    function ensureVideoPlay(videoElement) {
        videoElement.play().catch(function(error) {
            console.log('Auto-play was prevented:', error);
            document.addEventListener('click', function() {
                videoElement.play();
            }, { once: true });
        });
    }

    function getLockedItem(section) {
        if (!section) return null;
        const item = section.querySelector('.webm-item');
        if (!item) return null;

        const itemNumber = item.getAttribute('data-item');
        return completedAnimations.has(itemNumber) ? null : item;
    }

    function alignToSection(section) {
        if (!section) return;
        const sectionTop = section.offsetTop;

        if (Math.abs(window.scrollY - sectionTop) > 1) {
            window.scrollTo({
                top: sectionTop,
                behavior: 'auto'
            });
        }
    }

    function getActiveLockedSection() {
        const windowHeight = window.innerHeight;
        const activationLine = windowHeight * 0.45;
        const viewportLine = window.scrollY + activationLine;
        let activeSection = null;

        webmSections.forEach(function(section) {
            if (activeSection || !getLockedItem(section)) return;

            if (viewportLine >= section.offsetTop) {
                activeSection = section;
            }
        });

        return activeSection;
    }

    function updateScrollLock() {
        const nextLockedSection = getActiveLockedSection();

        if (nextLockedSection) {
            lockedSection = nextLockedSection;
            alignToSection(lockedSection);
            document.body.classList.add('story-scroll-locked');
        } else if (getActiveLockedSubmarineSection()) {
            lockedSection = null;
            lockedSubmarineSection = submarineSection;
            alignToSection(lockedSubmarineSection);
            document.body.classList.add('story-scroll-locked');
            playSubmarineOnce(false);
        } else {
            lockedSection = null;
            lockedSubmarineSection = null;
            document.body.classList.remove('story-scroll-locked');
        }
    }

    function releaseScrollLockIfComplete(itemNumber) {
        completedAnimations.add(itemNumber);

        if (lockedSection && !getLockedItem(lockedSection)) {
            lockedSection = null;
            document.body.classList.remove('story-scroll-locked');
        }
    }

    function getActiveLockedSubmarineSection() {
        if (!submarineSection || submarineAnimationCompleted) return null;

        const windowHeight = window.innerHeight;
        const activationLine = windowHeight * 0.45;
        const viewportLine = window.scrollY + activationLine;

        return viewportLine >= submarineSection.offsetTop ? submarineSection : null;
    }

    function playSubmarineOnce(restartFromBeginning = true) {
        if (!lockedSubmarineSection || submarineAnimationCompleted) return;
        if (submarineAnimationPlaying) return;

        submarineAnimationPlaying = true;
        dream1Video.removeAttribute('loop');
        dream1Video.loop = false;

        if (restartFromBeginning) {
            dream1Video.currentTime = 0;
        }

        dream1Video.play();

        function completeSubmarineAnimation() {
            if (submarineAnimationCompleted) return;

            dream1Video.removeEventListener('timeupdate', completeOnLastFrame);
            dream1Video.removeEventListener('ended', completeSubmarineAnimation);
            clearTimeout(submarineUnlockTimer);
            submarineAnimationPlaying = false;
            submarineAnimationCompleted = true;
            lockedSubmarineSection = null;
            dream1Video.loop = true;
            dream1Video.setAttribute('loop', '');
            dream1Video.play();
            document.body.classList.remove('story-scroll-locked');
        }

        function completeOnLastFrame() {
            if (!dream1Video.duration) return;

            if (dream1Video.currentTime >= dream1Video.duration - 0.1) {
                completeSubmarineAnimation();
            }
        }

        dream1Video.removeEventListener('timeupdate', completeOnLastFrame);
        dream1Video.removeEventListener('ended', completeSubmarineAnimation);
        clearTimeout(submarineUnlockTimer);

        dream1Video.addEventListener('timeupdate', completeOnLastFrame);
        dream1Video.addEventListener('ended', completeSubmarineAnimation);

        if (dream1Video.duration) {
            const remainingMs = Math.max(500, (dream1Video.duration - dream1Video.currentTime) * 1000 + 250);
            submarineUnlockTimer = setTimeout(completeSubmarineAnimation, remainingMs);
        }
    }

    function blockScrollEvent(event) {
        updateScrollLock();

        if (!lockedSection && !lockedSubmarineSection) return;

        event.preventDefault();
        alignToSection(lockedSection || lockedSubmarineSection);
    }

    function blockScrollKey(event) {
        const scrollKeys = ['ArrowDown', 'ArrowUp', 'PageDown', 'PageUp', 'Home', 'End', ' '];

        if (!scrollKeys.includes(event.key)) return;

        updateScrollLock();

        if (!lockedSection && !lockedSubmarineSection) return;

        event.preventDefault();
        alignToSection(lockedSection || lockedSubmarineSection);
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
                alignToSection(item.closest('section'));
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
                    releaseScrollLockIfComplete(itemNumber);

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

    dream1Video.addEventListener('dragstart', function(event) {
        event.preventDefault();
        isSubmarineDragActive = true;
        playSubmarineOnce();
    });

    dream1Video.addEventListener('mousedown', function(event) {
        dragStartX = event.clientX;
        dragStartY = event.clientY;
        isSubmarineDragActive = true;
    });

    dream1Video.addEventListener('touchstart', function(event) {
        if (!event.touches.length) return;
        dragStartX = event.touches[0].clientX;
        dragStartY = event.touches[0].clientY;
        isSubmarineDragActive = true;
    }, { passive: true });

    window.addEventListener('mousemove', function(event) {
        if (!isSubmarineDragActive) return;
        if (event.buttons !== 1) return;
        if (Math.abs(event.clientX - dragStartX) < 6 && Math.abs(event.clientY - dragStartY) < 6) return;
        playSubmarineOnce();
    });

    window.addEventListener('touchmove', function(event) {
        if (!isSubmarineDragActive) return;
        if (!event.touches.length) return;
        if (Math.abs(event.touches[0].clientX - dragStartX) < 6 && Math.abs(event.touches[0].clientY - dragStartY) < 6) return;
        playSubmarineOnce();
    }, { passive: true });

    window.addEventListener('mouseup', function() {
        isSubmarineDragActive = false;
    });

    window.addEventListener('touchend', function() {
        isSubmarineDragActive = false;
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
            text5Image, logoImage, itemGif
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

        updateScrollLock();

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
    window.addEventListener('wheel', blockScrollEvent, { passive: false });
    window.addEventListener('touchstart', function(event) {
        touchStartY = event.touches[0].clientY;
    }, { passive: true });
    window.addEventListener('touchmove', function(event) {
        if (Math.abs(event.touches[0].clientY - touchStartY) < 2) return;
        blockScrollEvent(event);
    }, { passive: false });
    window.addEventListener('keydown', blockScrollKey);

    setTimeout(handleScroll, 100);
});
