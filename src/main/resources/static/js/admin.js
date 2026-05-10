(function () {
    const state = {
        members: { page: 0, totalPages: 0, endpoint: '/admin-console/members', listId: 'members-list', metaId: 'members-page-meta', type: 'members' },
        concerns: { page: 0, totalPages: 0, endpoint: '/admin-console/concerns', listId: 'concerns-list', metaId: 'concerns-page-meta', type: 'concerns' },
        memberLoginLogs: { page: 0, totalPages: 0, endpoint: '/admin-console/member-login-logs', listId: 'member-login-logs-list', metaId: 'member-login-logs-page-meta', type: 'member-login-logs' },
        geminiRequestLogs: { page: 0, totalPages: 0, endpoint: '/admin-console/gemini-request-logs', listId: 'gemini-request-logs-list', metaId: 'gemini-request-logs-page-meta', type: 'gemini-request-logs' }
    };

    const summaryIds = {
        member: 'total-member-count',
        concern: 'total-concern-count',
        ai: 'total-ai-request-count'
    };

    document.addEventListener('DOMContentLoaded', () => {
        bindPaginationButtons();
        loadAll();
    });

    function bindPaginationButtons() {
        document.querySelectorAll('.page-btn[data-panel]').forEach((button) => {
            button.addEventListener('click', async () => {
                const panel = button.dataset.panel;
                const direction = button.dataset.direction;
                const panelState = state[camelize(panel)];
                if (!panelState) return;

                if (direction === 'prev' && panelState.page > 0) {
                    panelState.page -= 1;
                    await loadPanel(panelState);
                }

                if (direction === 'next' && panelState.page + 1 < panelState.totalPages) {
                    panelState.page += 1;
                    await loadPanel(panelState);
                }
            });
        });
    }

    async function loadAll() {
        await Promise.all([
            loadSummary(),
            loadPanel(state.members),
            loadPanel(state.concerns),
            loadPanel(state.memberLoginLogs),
            loadPanel(state.geminiRequestLogs)
        ]);
    }

    async function loadSummary() {
        try {
            const res = await fetch('/admin-console/totalMember-totalConcern-Count');
            if (!res.ok) throw new Error('summary load failed');
            const data = await res.json();
            setText(summaryIds.member, data.totalMemberCount ?? 0);
            setText(summaryIds.concern, data.totalConcernCount ?? 0);
            setText(summaryIds.ai, data.totalAiRequestCountTodayCount ?? 0);
        } catch (error) {
            setText(summaryIds.member, '-');
            setText(summaryIds.concern, '-');
            setText(summaryIds.ai, '-');
        }
    }

    async function loadPanel(panelState) {
        const listEl = document.getElementById(panelState.listId);
        const metaEl = document.getElementById(panelState.metaId);

        if (!listEl || !metaEl) return;

        listEl.innerHTML = renderLoading(panelState.type);

        try {
            const res = await fetch(`${panelState.endpoint}?page=${panelState.page}`);
            if (!res.ok) throw new Error('panel load failed');

            const page = await res.json();
            panelState.totalPages = page.totalPages ?? 0;
            panelState.page = page.number ?? panelState.page;

            metaEl.textContent = `${panelState.totalPages === 0 ? 0 : panelState.page + 1} / ${panelState.totalPages}`;
            listEl.innerHTML = renderPanelItems(panelState.type, page.content ?? []);
            updateButtons(panelState);
        } catch (error) {
            metaEl.textContent = '0 / 0';
            listEl.innerHTML = `<div class="admin-empty">데이터를 불러오지 못했습니다</div>`;
            updateButtons(panelState);
        }
    }

    function renderPanelItems(type, items) {
        if (!items.length) {
            return `<div class="admin-empty">표시할 데이터가 없습니다</div>`;
        }

        if (type === 'members') {
            return items.map((item) => `
                <div class="admin-item">
                    <div class="admin-item-title">${escapeHtml(item.memberEmail ?? '')}</div>
                    <div class="admin-item-subtitle">
                        <span>${escapeHtml(item.memberNickname ?? '')}</span>
                    </div>
                    <div class="admin-item-meta">
                        <span>${formatDateTime(item.createdAt)}</span>
                        <span>ID ${escapeHtml(String(item.memberId ?? ''))}</span>
                    </div>
                </div>
            `).join('');
        }

        if (type === 'concerns') {
            return items.map((item) => `
                <div class="admin-item">
                    <div class="admin-item-title">${escapeHtml(item.concernTitle ?? '')}</div>
                    <div class="admin-item-subtitle">
                        <span>${escapeHtml(item.memberNickname ?? '')}</span>
                        <span>${escapeHtml(item.memberEmail ?? '')}</span>
                        <span>${escapeHtml(item.luggageType ?? '')}</span>
                    </div>
                    <div class="admin-item-meta">
                        <span>${formatDateTime(item.createdAt)}</span>
                        ${item.deletedAt ? '<span class="status-badge status-badge--deleted">deleted</span>' : '<span></span>'}
                    </div>
                </div>
            `).join('');
        }

        if (type === 'member-login-logs') {
            return items.map((item) => `
                <div class="admin-item">
                    <div class="admin-item-title">${escapeHtml(item.loginTryId ?? '')}</div>
                    <div class="admin-item-subtitle">
                        <span>${escapeHtml(item.loginMemberNickname ?? '-') }</span>
                        <span>${escapeHtml(item.loginFailureReason ?? '-')}</span>
                        <span>${escapeHtml(item.loginTryIp ?? '-')}</span>
                    </div>
                    <div class="admin-item-meta">
                        <span>${formatDateTime(item.createdAt)}</span>
                        <span class="status-badge ${item.loginStatus ? 'status-badge--success' : 'status-badge--fail'}">
                            ${item.loginStatus ? 'success' : 'fail'}
                        </span>
                    </div>
                </div>
            `).join('');
        }

        if (type === 'gemini-request-logs') {
            return items.map((item) => `
                <div class="admin-item">
                    <div class="admin-item-title">member #${escapeHtml(String(item.requestMemberId ?? '-'))}</div>
                    <div class="admin-item-subtitle">
                        <span>${escapeHtml(item.requestMemberNickname ?? '-')}</span>
                        <span>${escapeHtml(item.requestFailureReason ?? '-')}</span>
                    </div>
                    <div class="admin-item-meta">
                        <span>${formatDateTime(item.createdAt)}</span>
                        <span class="status-badge ${item.requestStatus ? 'status-badge--success' : 'status-badge--fail'}">
                            ${item.requestStatus ? 'success' : 'fail'}
                        </span>
                    </div>
                </div>
            `).join('');
        }

        return `<div class="admin-empty">표시할 데이터가 없습니다</div>`;
    }

    function updateButtons(panelState) {
        const prevButton = document.querySelector(`.page-btn[data-panel="${panelState.type}"][data-direction="prev"]`);
        const nextButton = document.querySelector(`.page-btn[data-panel="${panelState.type}"][data-direction="next"]`);

        if (prevButton) prevButton.disabled = panelState.page <= 0;
        if (nextButton) nextButton.disabled = panelState.totalPages === 0 || panelState.page + 1 >= panelState.totalPages;
    }

    function renderLoading(type) {
        return `<div class="admin-empty">${escapeHtml(getPanelLabel(type))} 불러오는 중...</div>`;
    }

    function getPanelLabel(type) {
        if (type === 'members') return '회원';
        if (type === 'concerns') return '고민';
        if (type === 'member-login-logs') return '접속 로그';
        if (type === 'gemini-request-logs') return 'AI 요청 로그';
        return '데이터';
    }

    function camelize(panel) {
        if (panel === 'member-login-logs') return 'memberLoginLogs';
        if (panel === 'gemini-request-logs') return 'geminiRequestLogs';
        return panel;
    }

    function setText(id, value) {
        const el = document.getElementById(id);
        if (el) el.textContent = String(value);
    }

    function formatDateTime(value) {
        if (!value) return '-';
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) return '-';

        const pad = (n) => String(n).padStart(2, '0');
        return `${String(date.getFullYear()).slice(2)}.${pad(date.getMonth() + 1)}.${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
    }

    function escapeHtml(value) {
        return String(value)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;')
            .replaceAll("'", '&#39;');
    }
})();
