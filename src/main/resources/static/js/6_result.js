document.addEventListener('DOMContentLoaded', function() {
    const submarine = document.getElementById('submarine');
    const luggageCircle = document.getElementById('luggageCircle');
    const passwordBox = document.getElementById('passwordBox');
    const responseBox = document.getElementById('responseBox');
    const passwordInput = document.getElementById('passwordInput');
    const passwordSubmit = document.getElementById('passwordSubmit');
    const passwordTitle = document.getElementById('passwordTitle');
    const errorMsg = document.getElementById('errorMsg');
    const responseTitle = document.getElementById('responseTitle');
    const responseContent = document.getElementById('responseContent');
    const paginationControls = document.getElementById('paginationControls');
    const prevPageBtn = document.getElementById('prevPage');
    const nextPageBtn = document.getElementById('nextPage');
    const currentPageSpan = document.getElementById('currentPage');
    const totalPagesSpan = document.getElementById('totalPages');
    
    let isLuggageVisible = false;
    let currentSelectedLuggage = null;
    
    // 페이지네이션 변수
    const itemsPerPage = 5;
    let currentPage = 1;
    let totalPages = 1;
    let allLuggageItems = [];
    
    // 초기화 함수
    function init() {
        allLuggageItems = Array.from(document.querySelectorAll('.luggage-item'));
        totalPages = Math.ceil(allLuggageItems.length / itemsPerPage);
        totalPagesSpan.textContent = totalPages;
        
        // 페이지네이션이 필요한 경우만 버튼 표시 준비
        if (allLuggageItems.length > itemsPerPage) {
            // 페이지네이션 필요
        }
        
        // 초기 위치 설정 (숨겨진 상태)
        positionLuggage();
    }
    
    // 현재 페이지의 아이템만 표시
    function showCurrentPage() {
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        
        allLuggageItems.forEach((item, index) => {
            if (index >= startIndex && index < endIndex) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        });
        
        // 현재 페이지의 아이템들만 재배치
        positionLuggage();
        
        // 페이지 번호 업데이트
        currentPageSpan.textContent = currentPage;
        
        // 버튼 활성화/비활성화
        prevPageBtn.disabled = currentPage === 1;
        nextPageBtn.disabled = currentPage === totalPages;
    }
    
    // 가방들을 원형으로 배치하는 함수 (현재 페이지의 아이템만)
    function positionLuggage() {
        const activeItems = allLuggageItems.filter(item => item.classList.contains('active'));
        const radius = 190;
        const centerX = 0;
        const centerY = 0;
        
        activeItems.forEach((item, index) => {
            const angle = (index / activeItems.length) * 2 * Math.PI;
            const x = centerX + radius * Math.cos(angle);
            const y = centerY + radius * Math.sin(angle);
            
            item.style.transform = `translate(${x - 40}px, ${y - 40}px)`;
        });
    }
    
    // 이전 페이지
    prevPageBtn.addEventListener('click', function() {
        if (currentPage > 1) {
            currentPage--;
            showCurrentPage();
            hideAllBoxes();
            currentSelectedLuggage = null;
        }
    });
    
    // 다음 페이지
    nextPageBtn.addEventListener('click', function() {
        if (currentPage < totalPages) {
            currentPage++;
            showCurrentPage();
            hideAllBoxes();
            currentSelectedLuggage = null;
        }
    });
    
    // 잠수함 클릭 이벤트
    submarine.addEventListener('click', function() {
        if (!isLuggageVisible) {
            // 가방들 표시
            showCurrentPage();
            luggageCircle.classList.add('show');
            isLuggageVisible = true;
            
            // 페이지네이션이 필요한 경우 표시
            if (allLuggageItems.length > itemsPerPage) {
                paginationControls.classList.add('show');
            }
        } else {
            // 가방들 숨기기
            hideAllBoxes();
            luggageCircle.classList.remove('show');
            paginationControls.classList.remove('show');
            isLuggageVisible = false;
            currentSelectedLuggage = null;
            
            // 모든 아이템 비활성화
            allLuggageItems.forEach(item => item.classList.remove('active'));
        }
    });
    
    // 모든 박스 숨기기 (애니메이션 포함)
    function hideAllBoxes() {
        passwordBox.classList.remove('show');
        responseBox.classList.remove('show');
        errorMsg.classList.remove('show');
    }
    
    // 전체 클릭 이벤트 처리
    document.addEventListener('click', function(e) {
        // 가방 아이템 클릭
        if (e.target.closest('.luggage-item')) {
            e.stopPropagation();
            
            const luggageItem = e.target.closest('.luggage-item');
            
            // active 클래스가 없는 아이템은 클릭 불가 (현재 페이지가 아님)
            if (!luggageItem.classList.contains('active')) {
                return;
            }
            
            const username = luggageItem.getAttribute('data-username');
            const lid = luggageItem.getAttribute('data-lid');
            
            currentSelectedLuggage = luggageItem;
            
            // 응답 박스 먼저 숨기기 (새로운 가방 선택 시)
            responseBox.classList.remove('show');
            
            // 비밀번호 입력 박스 표시 (애니메이션)
            passwordTitle.textContent = `${username}의 고민`;
            passwordInput.value = '';
            errorMsg.classList.remove('show');
            
            setTimeout(() => {
                passwordBox.classList.add('show');
            }, 100);
            
            setTimeout(() => {
                passwordInput.focus();
            }, 500);
            
            return;
        }
        
        // 잠수함 클릭
        if (e.target.closest('.submarine')) {
            e.stopPropagation();
            return;
        }
        
        // 비밀번호 박스 내부 클릭
        if (e.target.closest('.password-box')) {
            e.stopPropagation();
            return;
        }
        
        // 응답 박스 내부 클릭
        if (e.target.closest('.response-box')) {
            e.stopPropagation();
            return;
        }
        
        // 페이지네이션 컨트롤 클릭
        if (e.target.closest('.pagination-controls')) {
            e.stopPropagation();
            return;
        }
        
        // 그 외 배경 클릭 시 모든 박스 숨기기
        if (passwordBox.classList.contains('show') || responseBox.classList.contains('show')) {
            hideAllBoxes();
            currentSelectedLuggage = null;
        }
    });
    
    // 비밀번호 확인 함수
    function checkPassword() {
        if (!currentSelectedLuggage) return;
        
        const enteredPassword = passwordInput.value.trim();
        const correctPassword = currentSelectedLuggage.getAttribute('data-password');
        const username = currentSelectedLuggage.getAttribute('data-username');
        const lid = currentSelectedLuggage.getAttribute('data-lid');
        
        if (!enteredPassword) {
            showError('비밀번호를 입력해주세요');
            hideResponseBox();
            return;
        }
        
        if (enteredPassword === correctPassword) {
            errorMsg.classList.remove('show');
            fetchLuggageResponse(lid, username);
        } else {
            showError('잘못된 비밀번호입니다');
            hideResponseBox();
        }
    }
    
    // 응답 박스 숨기기
    function hideResponseBox() {
        responseBox.classList.remove('show');
    }
    
    // 서버에서 응답 가져오기
    function fetchLuggageResponse(lid, username) {
        fetch(`/api/luggage/${lid}/response`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('API endpoint not found');
                }
                return response.text();
            })
            .then(responseText => {
                showResponse(username, responseText);
            })
            .catch(error => {
                console.log('API 요청 실패:', error);
                showResponse(username, '응답을 불러올 수 없습니다.');
            });
    }
    
    // 응답 표시 (애니메이션 포함)
    function showResponse(username, response) {
        responseTitle.textContent = `${username}의 고민에 대한 응답`;
        responseContent.textContent = response;
        
        setTimeout(() => {
            responseBox.classList.add('show');
        }, 200);
        
        passwordInput.value = '';
    }
    
    // 에러 메시지 표시 (애니메이션 포함)
    function showError(message) {
        errorMsg.textContent = message;
        errorMsg.classList.add('show');
        passwordInput.value = '';
        passwordInput.focus();
        
        setTimeout(() => {
            errorMsg.classList.remove('show');
        }, 3000);
    }
    
    // 비밀번호 확인 버튼 클릭
    passwordSubmit.addEventListener('click', checkPassword);
    
    // Enter 키로 비밀번호 확인
    passwordInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            checkPassword();
        }
    });
    
    // 초기화 실행
    init();
});

// ============================
// 사이드바 토글 함수
// ============================
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');

    if (sidebar.classList.contains('open')) {
        closeSidebar();
    } else {
        openSidebar();
    }
}

// 사이드바 열기
function openSidebar() {
    const sidebar = document.getElementById('sidebar');
    const menuItems = document.querySelectorAll('.menu-item');

    if (sidebar) {
        sidebar.classList.add('open');
    }

    menuItems.forEach((item, index) => {
        const delay = parseInt(item.dataset.delay) || 0;
        setTimeout(() => {
            item.classList.add('show');
        }, delay);
    });
}

// 사이드바 닫기
function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const menuItems = document.querySelectorAll('.menu-item');

    menuItems.forEach(item => {
        item.classList.remove('show');
    });

    sidebar.classList.remove('open');
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
