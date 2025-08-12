// music-manager.js - 사용자 선택 기반 음악 재생 (자동 재생 제거)
class MusicManager {
    constructor() {
        console.log('🎵 MusicManager 생성자 시작');
        this.audio = null;
        this.isPlaying = false;
        this.isMuted = true; // 기본값: 음악 꺼짐
        this.userInteracted = false;
        this.initAudio();
        this.loadSoundState();
        console.log('🎵 MusicManager 초기화 완료');
    }

    // 오디오 초기화
    initAudio() {
        console.log('🎵 오디오 초기화 시작');
        try {
            this.audio = new Audio('/music/synth1.mp3');
            this.audio.loop = true;
            this.audio.volume = 0.3; // 볼륨 30%

            console.log('🎵 Audio 객체 생성됨:', this.audio.src);

            // 로드 시작
            this.audio.addEventListener('loadstart', () => {
                console.log('🎵 음악 파일 로드 시작');
            });

            // 로드 완료 (자동 재생 제거)
            this.audio.addEventListener('canplaythrough', () => {
                console.log('🎵 음악 파일 로드 완료 - 재생 가능');
                // 자동 재생 제거 - 사용자가 버튼을 눌러야만 재생
            });

            // 재생 시작
            this.audio.addEventListener('play', () => {
                console.log('🎵 음악 재생 시작됨');
                this.isPlaying = true;
            });

            // 재생 일시정지
            this.audio.addEventListener('pause', () => {
                console.log('🎵 음악 일시정지됨');
                this.isPlaying = false;
            });

            // 에러 처리
            this.audio.addEventListener('error', (e) => {
                console.error('❌ 음악 파일 에러:', e);
                console.error('❌ 에러 코드:', this.audio.error?.code);
                console.error('❌ 에러 메시지:', this.audio.error?.message);
            });

            // 로드 실패
            this.audio.addEventListener('stalled', () => {
                console.warn('⚠️ 음악 파일 로드 지연됨');
            });

        } catch (error) {
            console.error('❌ Audio 객체 생성 실패:', error);
        }
    }

    // 음악 재생
    async play() {
        console.log('🎵 play() 호출됨');
        console.log('🎵 상태 - isMuted:', this.isMuted, 'userInteracted:', this.userInteracted);

        if (this.audio && !this.isMuted) {
            try {
                await this.audio.play();
                console.log('✅ 음악 재생 성공');
                this.isPlaying = true;
            } catch (error) {
                console.error('❌ 음악 재생 실패:', error);
                if (error.name === 'NotAllowedError') {
                    console.log('🔒 브라우저가 자동 재생을 차단했습니다. 사용자 클릭이 필요합니다.');
                }
            }
        } else {
            console.log('⏸️ 재생 조건 불충족 - audio:', !!this.audio, 'muted:', this.isMuted);
        }
    }

    // 음악 정지
    stop() {
        console.log('🎵 stop() 호출됨');
        if (this.audio) {
            this.audio.pause();
            this.isPlaying = false;
            console.log('⏹️ 음악 정지됨');
        }
    }

    // 음소거 토글 (메인 기능)
    toggleMute() {
        console.log('🎵 toggleMute() 호출됨');
        this.userInteracted = true; // 사용자가 상호작용했음을 표시
        this.isMuted = !this.isMuted;
        console.log('🎵 음소거 상태 변경:', this.isMuted);

        if (this.isMuted) {
            this.stop();
        } else {
            this.play();
        }

        this.saveSoundState();
        this.updateSoundButtons();
    }

    // 사운드 상태 저장
    saveSoundState() {
        localStorage.setItem('soundMuted', this.isMuted.toString());
        console.log('💾 사운드 상태 저장:', this.isMuted);
    }

    // 사운드 상태 불러오기
    loadSoundState() {
        const savedState = localStorage.getItem('soundMuted');
        if (savedState !== null) {
            this.isMuted = savedState === 'true';
            console.log('📖 저장된 사운드 상태 불러오기:', this.isMuted);
        } else {
            this.isMuted = true; // 기본값: 음악 꺼짐
            console.log('📖 기본 사운드 상태 설정 (꺼짐):', this.isMuted);
        }
    }

    // 사운드 버튼 업데이트
    updateSoundButtons() {
        const soundButtons = document.querySelectorAll('.sound-toggle-btn img');
        console.log('🎵 사운드 버튼 업데이트 - 버튼 수:', soundButtons.length);

        soundButtons.forEach(img => {
            if (this.isMuted) {
                // 음소거 상태 = ON 버튼 표시 (클릭하면 음악 켜짐)
                img.src = '/image/1_mainPage/사운드 ON.png';
                img.alt = '사운드 켜기';
                console.log('🔊 버튼: ON 이미지 표시 (음악 꺼짐 상태)');
            } else {
                // 음악 재생 상태 = OFF 버튼 표시 (클릭하면 음악 꺼짐)
                img.src = '/image/1_mainPage/사운드 OFF.png';
                img.alt = '사운드 끄기';
                console.log('🔇 버튼: OFF 이미지 표시 (음악 켜짐 상태)');
            }
        });
        console.log('🎵 버튼 이미지 업데이트 완료');
    }

    // 사용자 상호작용 후 음악 시작 (제거됨 - 버튼으로만 제어)
    startAfterUserInteraction() {
        console.log('🎵 사용자 상호작용 감지 (자동 재생 비활성화)');
        this.userInteracted = true;
        // 자동 재생 제거 - 오직 버튼 클릭으로만 음악 시작
    }

    // 디버그 정보 출력
    debugInfo() {
        console.log('🔍 디버그 정보:');
        console.log('- Audio 객체:', this.audio);
        console.log('- 재생 중:', this.isPlaying);
        console.log('- 음소거:', this.isMuted);
        console.log('- 사용자 상호작용:', this.userInteracted);
        console.log('- 오디오 준비 상태:', this.audio?.readyState);
        console.log('- 오디오 길이:', this.audio?.duration);
    }
}

// 전역 음악 매니저 생성
console.log('🎵 MusicManager 전역 생성 시작');
window.musicManager = new MusicManager();

// 페이지 로드 완료 후 초기화
document.addEventListener('DOMContentLoaded', () => {
    console.log('📄 페이지 로드 완료');

    // 버튼 상태 업데이트 (ON 이미지 표시)
    window.musicManager.updateSoundButtons();

    // 디버그 정보 출력 (개발용)
    setTimeout(() => {
        window.musicManager.debugInfo();
    }, 1000);

    // 자동 상호작용 감지 제거 - 오직 사운드 버튼으로만 제어
});

// 윈도우에 디버그 함수 추가 (개발용)
window.debugMusic = () => {
    window.musicManager.debugInfo();
};

console.log('🎵 music-manager.js 로드 완료 - 사용자 선택 기반 재생');