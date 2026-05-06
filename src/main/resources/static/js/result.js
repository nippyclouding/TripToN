document.addEventListener('DOMContentLoaded', function() {
    const submarine = document.getElementById('submarine');
    const luggageCircle = document.getElementById('luggageCircle');
    const paginationControls = document.getElementById('paginationControls');
    const prevPageBtn = document.getElementById('prevPage');
    const nextPageBtn = document.getElementById('nextPage');
    const currentPageSpan = document.getElementById('currentPage');
    const totalPagesSpan = document.getElementById('totalPages');

    let isLuggageVisible = false;

    // 페이지네이션 변수 (서버에서 렌더링된 값 사용)
    let currentPage = parseInt(currentPageSpan.textContent) - 1; // 화면은 1-based, 서버는 0-based
    let totalPages = parseInt(totalPagesSpan.textContent) || 1;

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

    // 페이지 이동
    function loadPage(page) {
        window.location.href = `/concern?page=${page}`;
    }

    // 페이지네이션 UI 업데이트
    function updatePagination() {
        prevPageBtn.disabled = currentPage === 0;
        nextPageBtn.disabled = currentPage >= totalPages - 1;

        if (totalPages > 1 && isLuggageVisible) {
            paginationControls.classList.add('show');
        }
    }

    // 이전 페이지
    prevPageBtn.addEventListener('click', function() {
        if (currentPage > 0) {
            loadPage(currentPage - 1);
        }
    });

    // 다음 페이지
    nextPageBtn.addEventListener('click', function() {
        if (currentPage < totalPages - 1) {
            loadPage(currentPage + 1);
        }
    });

    // 잠수함 클릭 이벤트
    submarine.addEventListener('click', function() {
        if (!isLuggageVisible) {
            luggageCircle.classList.add('show');
            isLuggageVisible = true;
            positionLuggage();

            if (totalPages > 1) {
                paginationControls.classList.add('show');
            }
            updatePagination();
        } else {
            luggageCircle.classList.remove('show');
            paginationControls.classList.remove('show');
            isLuggageVisible = false;
        }
    });

    // 가방 클릭 → 상세 페이지 이동
    document.addEventListener('click', function(e) {
        if (e.target.closest('.luggage-item')) {
            e.stopPropagation();
            const lid = e.target.closest('.luggage-item').getAttribute('data-lid');
            window.location.href = `/concern/${lid}`;
        }
    });
});
