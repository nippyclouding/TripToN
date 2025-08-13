// 공통 사이드바 기능
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebar.classList.contains('open')) {
        closeSidebar();
    } else {
        openSidebar();
    }
}

function openSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuItems = document.querySelectorAll('.menu-item');

    if (sidebar) {
        sidebar.classList.add('open');
    }

    if (overlay) {
        overlay.classList.add('show');
    }

    menuItems.forEach((item, index) => {
        const delay = parseInt(item.dataset.delay) || 0;
        setTimeout(() => {
            item.classList.add('show');
        }, delay);
    });
}

function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuItems = document.querySelectorAll('.menu-item');

    menuItems.forEach(item => {
        item.classList.remove('show');
    });

    sidebar.classList.remove('open');
    overlay.classList.remove('show');
}

// ESC 키로 사이드바 닫기
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeSidebar();
    }
});

// 메뉴 클릭시 페이지 이동
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.menu-item').forEach(item => {
        item.addEventListener('click', function() {
            const link = this.dataset.link;
            if (link) {
                window.location.href = link;
            }
        });
    });
});

// 공통 사운드 토글 함수
function toggleSound() {
    if (window.musicManager) {
        window.musicManager.toggleMute();
    }
}

// 기존 toggleSound 함수는 그대로 유지
function toggleSound() {
    if (window.musicManager) {
        window.musicManager.toggleMute();
    }
}

// 사운드 상태 아이콘 업데이트 함수 추가
function updateSoundStatusIcon(isMuted) {
    const statusIcon = document.getElementById('sound-status-icon');
    if (statusIcon) {
        if (isMuted) {
            statusIcon.src = '/image/sharedImage/line.png';
            statusIcon.alt = '음악 정지';
        } else {
            statusIcon.src = '/image/sharedImage/dot.gif';
            statusIcon.alt = '음악 재생';
        }
    }
}