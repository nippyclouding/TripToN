class MusicManager {
    constructor() {
        this.audio = null;
        this.isPlaying = false;
        this.isMuted = true;
        this.userInteracted = false;
        this.initAudio();
        this.loadSoundState();
    }

    initAudio() {
        try {
            this.audio = new Audio('/music/synth1.mp3');
            this.audio.loop = true;
            this.audio.volume = 0.3;

            this.audio.addEventListener('play', () => { this.isPlaying = true; });
            this.audio.addEventListener('pause', () => { this.isPlaying = false; });
            this.audio.addEventListener('error', () => {
                console.error('음악 파일 로드 오류:', this.audio.error?.code);
            });
        } catch (error) {
            console.error('Audio 초기화 실패:', error);
        }
    }

    async play() {
        if (!this.audio || this.isMuted) return;
        try {
            await this.audio.play();
            this.isPlaying = true;
        } catch (error) {
            console.error('음악 재생 실패:', error);
        }
    }

    stop() {
        if (this.audio) {
            this.audio.pause();
            this.isPlaying = false;
        }
    }

    toggleMute() {
        this.userInteracted = true;
        this.isMuted = !this.isMuted;
        this.isMuted ? this.stop() : this.play();
        this.saveSoundState();
        this.updateSoundButtons();
    }

    saveSoundState() {
        localStorage.setItem('soundMuted', this.isMuted.toString());
    }

    loadSoundState() {
        const savedState = localStorage.getItem('soundMuted');
        this.isMuted = savedState !== null ? savedState === 'true' : true;
    }

    updateSoundButtons() {
        if (typeof updateSoundStatusIcon === 'function') {
            updateSoundStatusIcon(this.isMuted);
        }
    }
}

window.musicManager = new MusicManager();

document.addEventListener('DOMContentLoaded', () => {
    window.musicManager.updateSoundButtons();
});
