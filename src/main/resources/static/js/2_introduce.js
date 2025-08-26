// 페이지 로드 완료 후 실행
document.addEventListener('DOMContentLoaded', function() {
    console.log('2_introduce 페이지 로드 완료');
    
    // DOM 요소들 가져오기
    const boomVideo = document.getElementById('boomVideo');
    const afterImage = document.getElementById('afterImage');
    
    // 요소들이 제대로 로드되었는지 확인
    if (!boomVideo || !afterImage) {
        console.error('일부 요소를 찾을 수 없습니다!');
        return;
    }
    
    console.log('모든 DOM 요소 로드 완료');
    
    // 비디오 로드 완료시 첫 프레임에서 정지
    boomVideo.addEventListener('loadeddata', function() {
        console.log('비디오 로드 완료 - 첫 프레임에서 정지');
        boomVideo.currentTime = 0; // 첫 프레임으로 이동
        boomVideo.pause(); // 정지 상태 유지
    });
    
    // 비디오 클릭 이벤트
    boomVideo.addEventListener('click', function() {
        console.log('비디오 클릭됨 - 재생 시작');
        
        // 클릭 비활성화 (재생 중 중복 클릭 방지)
        boomVideo.style.pointerEvents = 'none';
        boomVideo.classList.remove('clickable');
        
        // 비디오 재생
        boomVideo.play().then(() => {
            console.log('폭발 비디오 재생 시작');
        }).catch(error => {
            console.error('비디오 재생 실패:', error);
            // 비디오 재생 실패 시 바로 After 이미지 표시
            showAfterImage();
        });
    });
    
    // 비디오 시간 업데이트 이벤트 (타이밍 조절용)
    boomVideo.addEventListener('timeupdate', function() {
        // 비디오 총 길이의 60% 지점에서 after 이미지 표시 시작
        if (boomVideo.duration && boomVideo.currentTime >= boomVideo.duration * 0.6) {
            if (!afterImage.classList.contains('show')) {
                console.log('비디오 60% 지점 - After 이미지 표시 시작 (펑! 효과)');
                showAfterImage();
            }
        }
    });
    
    // 비디오 종료 이벤트 (비디오 완전 종료 시)
    boomVideo.addEventListener('ended', function() {
        console.log('폭발 비디오 재생 완료');
        // 비디오만 숨기기 (after 이미지는 이미 표시됨)
        boomVideo.classList.add('hidden');
    });
    
    // 비디오 오류 이벤트
    boomVideo.addEventListener('error', function() {
        console.error('비디오 오류 발생');
        showAfterImage();
    });
    
    // After 이미지 표시 함수
    function showAfterImage() {
        // 비디오는 계속 재생되게 두고, After 이미지만 표시
        afterImage.classList.remove('hidden');
        
        // 더 빠른 펑! 효과를 위해 지연시간 단축
        setTimeout(() => {
            afterImage.classList.add('show');
            console.log('After 이미지 펑! 애니메이션 시작 (비디오와 함께)');
        }, 10);
    }
    
    // 리셋 기능 (After 이미지 클릭 시)
    afterImage.addEventListener('click', function() {
        console.log('After 이미지 클릭 - 리셋 실행');
        resetAnimation();
    });
    
    // 스페이스바로 리셋
    document.addEventListener('keydown', function(event) {
        if (event.code === 'Space') {
            event.preventDefault();
            console.log('스페이스바 눌림 - 리셋 실행');
            resetAnimation();
        }
    });
    
    // 애니메이션 리셋 함수
    function resetAnimation() {
        // 비디오 리셋 및 첫 프레임으로 이동
        boomVideo.classList.remove('hidden');
        boomVideo.pause();
        boomVideo.currentTime = 0;
        boomVideo.style.pointerEvents = 'auto';
        boomVideo.classList.add('clickable');
        
        // After 이미지 숨기기
        afterImage.classList.add('hidden');
        afterImage.classList.remove('show');
        
        console.log('애니메이션 리셋 완료 - 첫 프레임 정지 상태');
    }
    
    console.log('모든 이벤트 리스너 설정 완료');
});