class MusicManager {
    constructor() {
        this.audioSrc = '/music/TripToN_Background.mp3';
        this.mutedStorageKey = 'soundMuted';
        this.timeStorageKey = 'backgroundMusicTime';
        this.audio = null;
        this.isPlaying = false;
        this.isMuted = true;
        this.userInteracted = false;
        this.initAudio();
        this.loadSoundState();
        this.bindPageLifecycle();
    }

    initAudio() {
        try {
            this.audio = new Audio(this.audioSrc);
            this.audio.loop = true;
            this.audio.volume = 0.3;
            this.audio.preload = 'auto';

            this.audio.addEventListener('play', () => { this.isPlaying = true; });
            this.audio.addEventListener('pause', () => { this.isPlaying = false; });
            this.audio.addEventListener('loadedmetadata', () => {
                this.restorePlaybackTime();
            });
            this.audio.addEventListener('timeupdate', () => {
                this.savePlaybackTime();
            });
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
            this.restorePlaybackTime();
            await this.audio.play();
            this.isPlaying = true;
        } catch (error) {
            console.error('음악 재생 실패:', error);
        }
    }

    stop() {
        if (this.audio) {
            this.savePlaybackTime();
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
        localStorage.setItem(this.mutedStorageKey, this.isMuted.toString());
    }

    loadSoundState() {
        const savedState = localStorage.getItem(this.mutedStorageKey);
        this.isMuted = savedState !== null ? savedState === 'true' : true;
    }

    savePlaybackTime() {
        if (!this.audio || Number.isNaN(this.audio.currentTime)) return;
        localStorage.setItem(this.timeStorageKey, String(this.audio.currentTime));
    }

    restorePlaybackTime() {
        if (!this.audio || !this.audio.duration) return;

        const savedTime = Number(localStorage.getItem(this.timeStorageKey));
        if (!Number.isFinite(savedTime) || savedTime <= 0) return;

        const nextTime = savedTime % this.audio.duration;
        if (Math.abs(this.audio.currentTime - nextTime) > 1) {
            this.audio.currentTime = nextTime;
        }
    }

    bindPageLifecycle() {
        window.addEventListener('pagehide', () => this.savePlaybackTime());
        window.addEventListener('beforeunload', () => this.savePlaybackTime());

        document.addEventListener('click', () => {
            if (!this.isMuted && !this.isPlaying) {
                this.play();
            }
        }, { once: true });
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
    window.musicManager.play();
});
