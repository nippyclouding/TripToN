document.addEventListener('DOMContentLoaded', function() {
    const movingText = document.querySelector('.moving-text');
    const img = document.querySelector('.moving-text img');

    // 가방 이미지 관련 요소들
    const bagImage = document.getElementById('bag-image');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');

    // 가방 이미지 배열
    const bagImages = [
        '/image/3_selectPage/베개가방_기본.png',
        '/image/3_selectPage/서류가방_기본.png',
        '/image/3_selectPage/세탁바구니가방_기본.png'
    ];

    // 아이템 소개 이미지 배열 (a, b, c 순서)
    const introduceImages = [
        '/image/3_selectPage/itemIntroduce1.png', // 베개가방 (a)
        '/image/3_selectPage/itemIntroduce2.png', // 서류가방 (b)
        '/image/3_selectPage/itemIntroduce3.png'  // 세탁바구니가방 (c)
    ];

    let currentIndex = 0;
    let isAnimating = false;

    // 화면 중앙 위치 계산 (기존 moving text용)
    function setPosition() {
        const windowHeight = window.innerHeight;
        const centerY = windowHeight / 2;

        movingText.style.top = (centerY - 200) + 'px';
        movingText.style.position = 'fixed';
        movingText.style.left = '0px';
        movingText.style.width = '100%';
        movingText.style.zIndex = '-1';
        movingText.style.pointerEvents = 'none';
    }

    // 애니메이션 시작 (기존 moving text용)
    function startAnimation() {
        const windowWidth = window.innerWidth;
        let currentPosition = windowWidth;

        function moveText() {
            currentPosition -= 1.5;

            if (currentPosition < -img.offsetWidth) {
                currentPosition = windowWidth;
            }

            img.style.left = currentPosition + 'px';
            img.style.position = 'absolute';
            img.style.maxHeight = '400px';

            requestAnimationFrame(moveText);
        }

        moveText();
    }

    // 가방 이미지 변경 함수
    function changeBagImage(direction) {
        if (isAnimating) return;

        isAnimating = true;

        // 애니메이션 클래스 추가
        if (direction === 'next') {
            bagImage.classList.add('bag-slide-left');
        } else {
            bagImage.classList.add('bag-slide-right');
        }

        // 애니메이션 중간에 이미지 변경
        setTimeout(() => {
            bagImage.src = bagImages[currentIndex];
        }, 250);

        // 애니메이션 완료 후 클래스 제거
        setTimeout(() => {
            bagImage.classList.remove('bag-slide-left', 'bag-slide-right');
            isAnimating = false;
        }, 500);
    }

    // 아이템 모달 열기 함수를 전역으로 등록
    window.openItemModal = function() {
        const modal = document.getElementById('itemModal');
        const introduceImg = document.getElementById('item-introduce-image');
        const luggageInput = document.getElementById('luggage-input');

        // 현재 인덱스에 맞는 소개 이미지 설정
        introduceImg.src = introduceImages[currentIndex];

        // 현재 인덱스에 맞는 가방 값 설정
        const luggageValues = ['LuggageA', 'LuggageB', 'LuggageC'];
        luggageInput.value = luggageValues[currentIndex];

        // 모달 표시
        modal.classList.add('show');

        // 바디 스크롤 방지
        document.body.style.overflow = 'hidden';
    }

    // 아이템 모달 닫기 함수 (전역 함수로 등록)
    window.closeItemModal = function() {
        const modal = document.getElementById('itemModal');

        // 닫기 애니메이션
        modal.classList.add('closing');

        setTimeout(() => {
            modal.classList.remove('show', 'closing');
            document.body.style.overflow = 'auto'; // 스크롤 복원
        }, 300);
    }

    // 다음 버튼 클릭
    nextBtn.addEventListener('click', function() {
        currentIndex = (currentIndex + 1) % bagImages.length;
        changeBagImage('next');
    });

    // 이전 버튼 클릭
    prevBtn.addEventListener('click', function() {
        currentIndex = (currentIndex - 1 + bagImages.length) % bagImages.length;
        changeBagImage('prev');
    });

    // 가방 이미지 클릭 시 모달 열기 (기존 코드 수정)
    bagImage.addEventListener('click', function() {
        openItemModal();
    });

    // ESC 키로 모달 닫기
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            const modal = document.getElementById('itemModal');
            if (modal.classList.contains('show')) {
                closeItemModal();
            }
        }
    });

    // 초기 설정
    setPosition();
    startAnimation();

    // 브라우저 크기 변경 시 위치 재조정
    window.addEventListener('resize', setPosition);
});