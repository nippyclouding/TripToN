class SoundToggle {
    constructor() {
        this.soundButton = document.getElementById('soundButton');
        this.particleContainer = document.getElementById('particleContainer');
        this.isOn = false; // 초기 상태는 OFF
        this.isDragging = false;
        this.startX = 0;
        this.currentX = 0;
        this.dragThreshold = 50; // 드래그 임계값 (픽셀)

        this.init();
    }

    init() {
        this.createDragIndicator();
        this.bindEvents();
    }

    createDragIndicator() {
        // 드래그 인디케이터 생성
        const indicator = document.createElement('div');
        indicator.className = 'drag-indicator';
        indicator.innerHTML = '<div class="drag-progress"></div>';
        this.soundButton.parentElement.appendChild(indicator);

        this.dragIndicator = indicator;
        this.dragProgress = indicator.querySelector('.drag-progress');
    }

    bindEvents() {
        // 마우스 이벤트
        this.soundButton.addEventListener('mousedown', this.handleMouseDown.bind(this));
        document.addEventListener('mousemove', this.handleMouseMove.bind(this));
        document.addEventListener('mouseup', this.handleMouseUp.bind(this));

        // 터치 이벤트 (모바일 지원)
        this.soundButton.addEventListener('touchstart', this.handleTouchStart.bind(this), { passive: false });
        document.addEventListener('touchmove', this.handleTouchMove.bind(this), { passive: false });
        document.addEventListener('touchend', this.handleTouchEnd.bind(this));
    }

    // 마우스 이벤트 핸들러
    handleMouseDown(e) {
        e.preventDefault();
        this.startDrag(e.clientX);
    }

    handleMouseMove(e) {
        if (this.isDragging) {
            e.preventDefault();
            this.updateDrag(e.clientX);
        }
    }

    handleMouseUp(e) {
        if (this.isDragging) {
            this.endDrag();
        }
    }

    // 터치 이벤트 핸들러
    handleTouchStart(e) {
        e.preventDefault();
        const touch = e.touches[0];
        this.startDrag(touch.clientX);
    }

    handleTouchMove(e) {
        if (this.isDragging) {
            e.preventDefault();
            const touch = e.touches[0];
            this.updateDrag(touch.clientX);
        }
    }

    handleTouchEnd(e) {
        if (this.isDragging) {
            this.endDrag();
        }
    }

    startDrag(clientX) {
        this.isDragging = true;
        this.startX = clientX;
        this.currentX = clientX;

        this.soundButton.classList.add('dragging');
        this.dragIndicator.classList.add('active');

        // 커서 변경
        document.body.style.cursor = 'grabbing';
    }

    updateDrag(clientX) {
        this.currentX = clientX;
        const deltaX = Math.abs(this.currentX - this.startX);

        // 드래그 진행률 계산 (0-100%)
        const progress = Math.min((deltaX / this.dragThreshold) * 100, 100);
        this.dragProgress.style.width = `${progress}%`;

        // 버튼 위치 약간 이동 (시각적 피드백)
        const moveOffset = Math.min(deltaX * 0.1, 10);
        const direction = this.currentX > this.startX ? 1 : -1;
        this.soundButton.style.transform = `translateX(${moveOffset * direction}px)`;
    }

    endDrag() {
        const deltaX = Math.abs(this.currentX - this.startX);

        // 임계값을 넘었다면 토글
        if (deltaX >= this.dragThreshold) {
            this.toggleSound();
        }

        // 드래그 상태 리셋
        this.resetDragState();
    }

    resetDragState() {
        this.isDragging = false;
        this.soundButton.classList.remove('dragging');
        this.dragIndicator.classList.remove('active');
        this.dragProgress.style.width = '0%';
        this.soundButton.style.transform = 'translateX(0)';
        document.body.style.cursor = 'default';
    }

    toggleSound() {
        this.isOn = !this.isOn;

        // 이미지 변경
        const newSrc = this.isOn
            ? '/image/2_introducePage/사운드 ON.png'
            : '/image/2_introducePage/사운드 OFF.png';

        this.soundButton.src = newSrc;

        // 토글 애니메이션
        this.soundButton.classList.add('sound-toggle');

        // 펑 효과 생성
        this.createBurstEffect();

        // 애니메이션 클래스 제거
        setTimeout(() => {
            this.soundButton.classList.remove('sound-toggle');
        }, 600);

        console.log(`Sound is now ${this.isOn ? 'ON' : 'OFF'}`);
    }

    createBurstEffect() {
        const buttonRect = this.soundButton.getBoundingClientRect();
        const centerX = buttonRect.left + buttonRect.width / 2;
        const centerY = buttonRect.top + buttonRect.height / 2;

        // 파티클 개수
        const particleCount = 15;

        for (let i = 0; i < particleCount; i++) {
            this.createParticle(centerX, centerY);
        }
    }

    createParticle(x, y) {
        const particle = document.createElement('div');
        particle.className = 'particle colorful';

        // 랜덤 방향과 거리
        const angle = (Math.PI * 2 * Math.random());
        const velocity = 50 + Math.random() * 100;
        const size = 4 + Math.random() * 8;

        // 파티클 스타일 설정
        particle.style.left = `${x}px`;
        particle.style.top = `${y}px`;
        particle.style.width = `${size}px`;
        particle.style.height = `${size}px`;

        // 파티클을 컨테이너에 추가
        this.particleContainer.appendChild(particle);

        // 애니메이션 설정
        const endX = x + Math.cos(angle) * velocity;
        const endY = y + Math.sin(angle) * velocity;

        // 커스텀 애니메이션
        particle.animate([
            {
                transform: `translate(0, 0) scale(0) rotate(0deg)`,
                opacity: 1
            },
            {
                transform: `translate(${endX - x}px, ${endY - y}px) scale(1.2) rotate(180deg)`,
                opacity: 0.8,
                offset: 0.5
            },
            {
                transform: `translate(${(endX - x) * 1.5}px, ${(endY - y) * 1.5}px) scale(0) rotate(360deg)`,
                opacity: 0
            }
        ], {
            duration: 800,
            easing: 'cubic-bezier(0.25, 0.46, 0.45, 0.94)'
        });

        // 파티클 제거
        setTimeout(() => {
            if (particle.parentNode) {
                particle.parentNode.removeChild(particle);
            }
        }, 800);
    }

    // 진동 효과 (지원되는 기기에서)
    vibrate() {
        if ('vibrate' in navigator) {
            navigator.vibrate(100);
        }
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    new SoundToggle();

    // 로딩 애니메이션 완료 후 힌트 애니메이션
    setTimeout(() => {
        const hint = document.querySelector('.drag-hint');
        if (hint) {
            hint.style.animation = 'pulse 2s ease-in-out infinite';
        }
    }, 2000);
});

// 힌트 펄스 애니메이션 추가
const style = document.createElement('style');
style.textContent = `
    @keyframes pulse {
        0%, 100% { opacity: 0.8; transform: scale(1); }
        50% { opacity: 1; transform: scale(1.05); }
    }
`;
document.head.appendChild(style);