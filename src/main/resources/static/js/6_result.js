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
    let currentPage = 0; // 0-based (서버와 동일)
    let totalPages = parseInt(totalPagesSpan.textContent) || 1;

    // 현재 페이지에 표시된 아이템들
    let displayedItems = [];

    // 가방들을 원형으로 배치하는 함수
    function positionLuggage() {
        const items = luggageCircle.querySelectorAll('.luggage-item');
        const radius = 190;

        items.forEach((item, index) => {
            const angle = (index / items.length) * 2 * Math.PI;
            const x = radius * Math.cos(angle);
            const y = radius * Math.sin(angle);

            item.style.transform = `translate(${x - 40}px, ${y - 40}px)`;
            item.classList.add('active');
        });
    }

    // 서버에서 페이지 데이터 가져와서 렌더링
    function loadPage(page) {
        fetch(`/api/luggage?page=${page}`)
            .then(response => response.json())
            .then(data => {
                totalPages = data.totalPages;
                currentPage = data.currentPage;

                renderLuggageItems(data.content);
                updatePagination();
            })
            .catch(error => {
                console.log('페이지 로드 실패:', error);
            });
    }

    // 가방 아이템들을 DOM에 렌더링
    function renderLuggageItems(items) {
        // 기존 아이템 제거
        luggageCircle.innerHTML = '';

        items.forEach(item => {
            const div = document.createElement('div');
            div.className = 'luggage-item';
            div.setAttribute('data-lid', item.lid);
            div.setAttribute('data-username', item.username);

            div.innerHTML = `
                <img src="/image/sharedImage/item${item.luggageTypeOrdinal}.png"
                     alt="${item.luggageType}"
                     class="luggage-img"/>
                <div class="username-label">${item.username}</div>
            `;

            luggageCircle.appendChild(div);
        });

        // 원형 배치
        positionLuggage();
    }

    // 페이지네이션 UI 업데이트
    function updatePagination() {
        currentPageSpan.textContent = currentPage + 1; // 사용자에게는 1-based로 표시
        totalPagesSpan.textContent = totalPages;
        prevPageBtn.disabled = currentPage === 0;
        nextPageBtn.disabled = currentPage >= totalPages - 1;

        // 페이지가 2개 이상이면 페이지네이션 표시
        if (totalPages > 1 && isLuggageVisible) {
            paginationControls.classList.add('show');
        }
    }

    // 이전 페이지
    prevPageBtn.addEventListener('click', function() {
        if (currentPage > 0) {
            hideAllBoxes();
            currentSelectedLuggage = null;
            loadPage(currentPage - 1);
        }
    });

    // 다음 페이지
    nextPageBtn.addEventListener('click', function() {
        if (currentPage < totalPages - 1) {
            hideAllBoxes();
            currentSelectedLuggage = null;
            loadPage(currentPage + 1);
        }
    });

    // 잠수함 클릭 이벤트
    submarine.addEventListener('click', function() {
        if (!isLuggageVisible) {
            // 첫 페이지 로드 후 표시
            luggageCircle.classList.add('show');
            isLuggageVisible = true;

            // 초기 페이지 로드 (서버렌더링된 아이템이 있으면 배치만, 없으면 fetch)
            const existingItems = luggageCircle.querySelectorAll('.luggage-item');
            if (existingItems.length > 0) {
                positionLuggage();
            } else {
                loadPage(0);
            }

            if (totalPages > 1) {
                paginationControls.classList.add('show');
            }
            updatePagination();
        } else {
            // 가방들 숨기기
            hideAllBoxes();
            luggageCircle.classList.remove('show');
            paginationControls.classList.remove('show');
            isLuggageVisible = false;
            currentSelectedLuggage = null;
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

    // 비밀번호 확인 함수 (서버에서 BCrypt 검증)
    function checkPassword() {
        if (!currentSelectedLuggage) return;

        const enteredPassword = passwordInput.value.trim();
        const username = currentSelectedLuggage.getAttribute('data-username');
        const lid = currentSelectedLuggage.getAttribute('data-lid');

        if (!enteredPassword) {
            showError('비밀번호를 입력해주세요');
            hideResponseBox();
            return;
        }

        fetch('/verify-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ lid: lid, password: enteredPassword })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                errorMsg.classList.remove('show');
                fetchLuggageResponse(lid, username);
            } else {
                showError('잘못된 비밀번호입니다');
                hideResponseBox();
            }
        })
        .catch(error => {
            console.log('비밀번호 검증 실패:', error);
            showError('검증 중 오류가 발생했습니다');
            hideResponseBox();
        });
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
});
