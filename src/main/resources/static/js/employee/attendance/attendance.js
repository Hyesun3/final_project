// 시계 
function updateClock() {
	var now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes();
    let seconds = now.getSeconds();
    	
    if(hours < 10){
		hours = '0' + hours;
	}
	if(minutes < 10){
		minutes = '0' + minutes;
	}
	if(seconds < 10){
		seconds = '0' + seconds;
	}
    var clock = hours + ':' + minutes + ':' + seconds;
    document.getElementById('clock').textContent = clock;
}

// 매 초마다 업데이트 
setInterval(updateClock, 1000);

// 페이지 로드 시에도 시계 업데이트
document.addEventListener('DOMContentLoaded', function() {
	updateClock();
});

// 출근
document.addEventListener('DOMContentLoaded', function() {
    // HTML 요소에서 memberNo 값을 읽어오기
    var memberNo = document.getElementById('attendance_memberNo').value;

    // 버튼 클릭 이벤트 핸들러
    document.getElementById('check_in_button').addEventListener('click', function() {
		event.preventDefault();
		
        var csrfToken = document.querySelector('input[name="_csrf"]').value;
        var url = '/attendance/checkIn';
        var jsonData = JSON.stringify({ memberNo: memberNo });

        // 버튼 비활성화 및 스타일 업데이트
        var button = document.getElementById('check_in_button');
        button.disabled = true;
        button.style.backgroundColor = '#eee';

        // AJAX 요청 보내기
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest',
                'X-CSRF-TOKEN': csrfToken
            },
            body: jsonData
        })
        .then(response => response.json())
        .then(data => {
            if (data.res_code === '200') {
                Swal.fire({
                    icon: 'success',
                    title: '출석 확인',
                    text: data.res_msg,
                    confirmButtonText: '확인'
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '오류',
                    text: data.res_msg,
                    confirmButtonText: '확인'
                });
            }
        });
    });
});