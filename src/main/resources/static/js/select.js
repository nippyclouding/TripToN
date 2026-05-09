document.addEventListener('DOMContentLoaded', function() {
    const ANIM_IMAGE_SWAP_MS = 250;
    const ANIM_SLIDE_DURATION_MS = 500;
    const MODAL_CLOSE_DURATION_MS = 300;
    const FOCUS_DELAY_MS = 100;
    const MOVING_TEXT_HEIGHT_PX = 800;
    const MOVING_TEXT_SPEED = 1.5;

    const movingText = document.querySelector('.moving-text');
    const img = document.querySelector('.moving-text img');

    const bagImage = document.getElementById('bag-image');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');

    const bagImages = [
        '/image/select/item1.png',  // 베개가방
        '/image/select/item2.png',  // 서류가방
        '/image/select/item3.png'   // 세탁바구니가방
    ];

    const storyImages = [
        '/image/select/itemIntroduce1.png', // 베개가방 -> item1
        '/image/select/itemIntroduce2.png', // 서류가방 -> item2
        '/image/select/itemIntroduce3.png'  // 세탁바구니가방 -> item3
    ];

    let currentIndex = 0;
    let isAnimating = false;

    function setPosition() {
        const windowHeight = window.innerHeight;
        const centerY = windowHeight / 2;

        movingText.style.position = 'fixed';
        movingText.style.left = '0px';
        movingText.style.width = '100%';
        movingText.style.zIndex = '-1';
        movingText.style.pointerEvents = 'none';
        movingText.style.top = (centerY - MOVING_TEXT_HEIGHT_PX / 2) + 'px';

        img.style.top = '0px';
    }

    function startAnimation() {
        const windowWidth = window.innerWidth;
        let currentPosition = windowWidth;

        function moveText() {
            currentPosition -= MOVING_TEXT_SPEED;

            if (currentPosition < -img.offsetWidth) {
                currentPosition = windowWidth;
            }

            img.style.left = currentPosition + 'px';
            img.style.position = 'absolute';
            img.style.maxHeight = MOVING_TEXT_HEIGHT_PX + 'px';

            requestAnimationFrame(moveText);
        }

        moveText();
    }

    function changeBagImage(direction) {
        if (isAnimating) return;

        isAnimating = true;

        if (direction === 'next') {
            bagImage.classList.add('bag-slide-left');
        } else {
            bagImage.classList.add('bag-slide-right');
        }

        setTimeout(() => {
            bagImage.src = bagImages[currentIndex];
        }, ANIM_IMAGE_SWAP_MS);

        setTimeout(() => {
            bagImage.classList.remove('bag-slide-left', 'bag-slide-right');
            isAnimating = false;
        }, ANIM_SLIDE_DURATION_MS);
    }

    window.openItemModal = function() {
        const modal = document.getElementById('itemModal');
        const storyImg = document.getElementById('item-story-image');
        const luggageInput = document.getElementById('luggage-input');

        storyImg.src = storyImages[currentIndex];

        const luggageValues = ['a', 'b', 'c'];
        luggageInput.value = luggageValues[currentIndex];

        modal.classList.add('show');
        document.body.style.overflow = 'hidden';
    }

    window.closeItemModal = function() {
        const modal = document.getElementById('itemModal');

        modal.classList.add('closing');

        setTimeout(() => {
            modal.classList.remove('show', 'closing');
            document.body.style.overflow = 'auto';
        }, MODAL_CLOSE_DURATION_MS);
    }

    window.showInputModal = function() {
        const inputModal = document.getElementById('inputModal');
        const luggageInput = document.getElementById('luggage-input');
        const currentLuggage = document.getElementById('luggage-input').value;

        luggageInput.value = currentLuggage;

        inputModal.classList.add('show');
        document.body.style.overflow = 'auto';

        setTimeout(() => {
            document.getElementById('concern-input').focus();
        }, FOCUS_DELAY_MS);
    }

    window.closeInputModal = function() {
        const inputModal = document.getElementById('inputModal');

        inputModal.classList.add('closing');

        setTimeout(() => {
            inputModal.classList.remove('show', 'closing');
            document.getElementById('title-input').value = '';
            document.getElementById('concern-input').value = '';
        }, MODAL_CLOSE_DURATION_MS);
    }

    let isSubmitting = false;

    window.submitConcern = function() {
        if (isSubmitting) return;

        const title   = document.getElementById('title-input').value.trim();
        const content = document.getElementById('concern-input').value.trim();

        if (title === '') {
            alert('고민 제목을 입력해주세요.');
            document.getElementById('title-input').focus();
            return;
        }

        if (content === '') {
            alert('고민 내용을 입력해주세요.');
            document.getElementById('concern-input').focus();
            return;
        }

        isSubmitting = true;

        const arrowBtn = document.querySelector('.input-modal-arrow-btn');
        arrowBtn.style.opacity = '0.4';
        arrowBtn.style.pointerEvents = 'none';
        arrowBtn.querySelector('img').alt = '처리 중...';

        document.getElementById('concern-form').submit();
    }

    nextBtn.addEventListener('click', function() {
        currentIndex = (currentIndex + 1) % bagImages.length;
        changeBagImage('next');
    });

    prevBtn.addEventListener('click', function() {
        currentIndex = (currentIndex - 1 + bagImages.length) % bagImages.length;
        changeBagImage('prev');
    });

    bagImage.addEventListener('click', function() {
        openItemModal();
    });

    // ESC / Enter 키 처리 (사이드바 ESC는 shared.js에서 별도 처리)
    document.addEventListener('keydown', function(e) {
        const itemModal = document.getElementById('itemModal');
        const inputModal = document.getElementById('inputModal');

        if (e.key === 'Escape') {
            if (inputModal.classList.contains('show')) {
                closeInputModal();
            } else if (itemModal.classList.contains('show')) {
                closeItemModal();
            }
        } else if (e.key === 'Enter' && inputModal.classList.contains('show')) {
            e.preventDefault();
            submitConcern();
        }
    });

    setPosition();
    startAnimation();

    window.addEventListener('resize', setPosition);
});
