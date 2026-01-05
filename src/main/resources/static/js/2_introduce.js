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
    
    // 동영상 자동 재생 보장
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

    // webm 비디오 클릭 이벤트 및 item 이미지 팝업
    webmItems.forEach(function(item) {
        const video = item.querySelector('.webm-video');
        const itemNumber = item.getAttribute('data-item');
        const popup = document.getElementById('item' + itemNumber + '-popup');
        let isPlaying = false;
        
        item.addEventListener('click', function() {
            // 재생 중이 아닐 때만 클릭 가능
            if (!isPlaying && video.paused) {
                // 재생 상태로 변경
                isPlaying = true;
                item.classList.add('playing');
                
                // 비디오 재생
                video.play();
                
                // 비디오 종료 0.5초 전에 item 이미지 팝업 표시
                video.addEventListener('timeupdate', function() {
                    const timeLeft = video.duration - video.currentTime;
                    if (timeLeft <= 0.5 && timeLeft > 0 && !popup.classList.contains('show')) {
                        popup.classList.remove('hide');
                        popup.classList.add('show');
                    }
                });
                
                // 비디오 종료 시 처리
                video.addEventListener('ended', function() {
                    // 1초 후 팝업 사라지고 클릭 다시 활성화
                    setTimeout(function() {
                        hidePopup(popup);
                        
                        // webm 페이드아웃 후 리셋하고 페이드인
                        item.style.transition = 'opacity 0.3s ease-in-out';
                        item.style.opacity = '0';
                        
                        setTimeout(function() {
                            isPlaying = false;
                            item.classList.remove('playing');
                            video.currentTime = 0; // 비디오 처음으로 되돌리기
                            
                            // 페이드인으로 다시 나타나기
                            item.style.opacity = '1';
                            
                            // 애니메이션 완료 후 transition 제거
                            setTimeout(function() {
                                item.style.transition = '';
                            }, 300);
                        }, 300);
                    }, 1000);
                }, { once: true }); // 한 번만 실행되도록
            }
        });
    });

    // item.gif 클릭 이벤트
    let gifPlaying = false;
    itemGif.addEventListener('click', function() {
        if (!gifPlaying) {
            // GIF 재시작을 위해 src를 다시 설정
            const gifSrc = itemGif.src;
            itemGif.src = '';
            itemGif.src = gifSrc;
            gifPlaying = true;
            
            // 3초 후 재생 상태 리셋 (GIF가 끝났다고 가정)
            setTimeout(function() {
                gifPlaying = false;
            }, 3000);
        }
    });

    // 팝업 숨기기 함수
    function hidePopup(popup) {
        popup.classList.remove('show');
        popup.classList.add('hide');
        setTimeout(function() {
            popup.classList.remove('hide');
        }, 300);
    }

    // 요소가 화면에 보이는지 확인하는 함수
    function isElementInViewport(element, threshold = 0.3) {
        if (!element) return false;
        const rect = element.getBoundingClientRect();
        const windowHeight = window.innerHeight;
        
        return (
            rect.top < windowHeight * (1 - threshold) &&
            rect.bottom > windowHeight * threshold
        );
    }

    // 스크롤 이벤트 리스너
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        const windowHeight = window.innerHeight;
        
        // 스크롤 진행도 계산 (0에서 1 사이)
        const scrollProgress = Math.min(scrollTop / windowHeight, 1);
        
        // 그라데이션 오버레이 표시 (더 빠르게 나타나도록)
        if (scrollProgress > 0.02) {  // 0.1에서 0.02로 변경 (2%만 스크롤해도 나타남)
            gradientOverlay.classList.add('visible');
        } else {
            gradientOverlay.classList.remove('visible');
        }
        
        // 각 요소들 개별 체크 (매번 체크해서 재애니메이션)
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
        
        // 각 webm 아이템 개별 체크
        webmItems.forEach(function(item) {
            if (isElementInViewport(item)) {
                if (!item.classList.contains('visible')) {
                    item.classList.add('visible');
                    // webm이 보일 때마다 비디오 리셋
                    const video = item.querySelector('.webm-video');
                    if (video.ended) {
                        video.currentTime = 0;
                    }
                }
            } else {
                item.classList.remove('visible');
            }
        });
        
        // item.gif가 화면을 벗어났다가 다시 들어오면 리셋
        if (itemGif && !isElementInViewport(itemGif)) {
            gifPlaying = false;
        }
    });

    // 페이지 로드 시 초기 상태 확인
    function checkInitialScroll() {
        window.dispatchEvent(new Event('scroll'));
    }
    
    // 초기 스크롤 상태 확인
    setTimeout(checkInitialScroll, 100);
    
    // dream1 동영상 메타데이터 로드 후 재생 보장
    dream1Video.addEventListener('loadedmetadata', function() {
        dream1Video.play();
    });
    
    // dream1 동영상 일시정지 방지
    dream1Video.addEventListener('pause', function() {
        dream1Video.play();
    });
    
    // 동영상 메타데이터 로드 후 재생 보장 (기존 dream6)
    video.addEventListener('loadedmetadata', function() {
        video.play();
    });
    
    // 동영상 일시정지 방지 (기존 dream6)
    video.addEventListener('pause', function() {
        video.play();
    });
});