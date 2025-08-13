document.addEventListener('DOMContentLoaded', function() {
    const colorButtons = document.querySelectorAll('.color-btn');
    const selectedColorSpan = document.getElementById('selectedColor');
    const submitBtn = document.getElementById('submitBtn');
    const form = document.getElementById('colorForm');

    let selectedColor = null;

    // 색깔 버튼 클릭 이벤트
    colorButtons.forEach(button => {
        button.addEventListener('click', function() {
            const color = this.getAttribute('data-color');

            // 모든 버튼의 선택 상태 제거
            colorButtons.forEach(btn => btn.classList.remove('selected'));

            if (selectedColor === color) {
                // 같은 색깔을 다시 클릭하면 선택 해제
                selectedColor = null;
            } else {
                // 새로운 색깔 선택
                this.classList.add('selected');
                selectedColor = color;
            }

            updateSelectedDisplay();
        });
    });

    // 선택된 색깔 표시 업데이트
    function updateSelectedDisplay() {
        if (selectedColor) {
            selectedColorSpan.textContent = getColorName(selectedColor);
            selectedColorSpan.style.color = getColorCode(selectedColor);
            selectedColorSpan.style.fontWeight = 'bold';
            submitBtn.disabled = false;
        } else {
            selectedColorSpan.textContent = '없음';
            selectedColorSpan.style.color = '#666';
            selectedColorSpan.style.fontWeight = 'normal';
            submitBtn.disabled = true;
        }
    }

    // 색깔 이름 반환
    function getColorName(color) {
        const colorNames = {
            'red': '빨간색',
            'green': '초록색',
            'blue': '파란색'
        };
        return colorNames[color];
    }

    // 색깔 코드 반환
    function getColorCode(color) {
        const colorCodes = {
            'red': '#ff4444',
            'green': '#44ff44',
            'blue': '#4444ff'
        };
        return colorCodes[color];
    }

    // 폼 제출 시 선택된 색깔을 hidden input으로 추가
    form.addEventListener('submit', function(e) {
        if (!selectedColor) {
            e.preventDefault();
            alert('색깔을 선택해주세요.');
            return;
        }

        // 기존 hidden input 제거
        const existingInput = form.querySelector('input[name="selectedColor"]');
        if (existingInput) {
            existingInput.remove();
        }

        // 선택된 색깔을 hidden input으로 추가
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'selectedColor';
        hiddenInput.value = selectedColor;
        form.appendChild(hiddenInput);
    });
});