// 사이드바 토글 함수
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebar.classList.contains('open')) {
        closeSidebar();
    } else {
        openSidebar();
    }
}

// 사이드바 열기
function openSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuItems = document.querySelectorAll('.menu-item');

    console.log('사이드바 열기 시도'); // 디버그용

    if (sidebar) {
        sidebar.classList.add('open');
        console.log('사이드바 열림'); // 디버그용
    }

    if (overlay) {
        overlay.classList.add('show');
    } else {
        console.log('오버레이를 찾을 수 없음'); // 디버그용
    }

    // 메뉴 아이템들을 순차적으로 나타내기
    menuItems.forEach((item, index) => {
        const delay = parseInt(item.dataset.delay) || 0;
        setTimeout(() => {
            item.classList.add('show');
            console.log('메뉴 아이템 표시:', item.querySelector('img').alt); // 디버그용
        }, delay);
    });
}

// 사이드바 닫기
function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuItems = document.querySelectorAll('.menu-item');

    // 메뉴 아이템들 숨기기
    menuItems.forEach(item => {
        item.classList.remove('show');
    });

    // 사이드바와 오버레이 숨기기
    sidebar.classList.remove('open');
    overlay.classList.remove('show');
}

// ESC 키로 사이드바 닫기
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeSidebar();
    }
});

// 메뉴 아이템 클릭시 페이지 이동
document.querySelectorAll('.menu-item').forEach(item => {
    item.addEventListener('click', function() {
        const link = this.dataset.link;
        if (link) {
            window.location.href = link;
        }
    });
});
