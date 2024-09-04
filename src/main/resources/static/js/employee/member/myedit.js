// 주소 api
function sample4_execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {

			var roadAddr = data.roadAddress;
			var extraRoadAddr = '';

			if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
				extraRoadAddr += data.bname;
			}
			if (data.buildingName !== '' && data.apartment === 'Y') {
				extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
			}
			if (extraRoadAddr !== '') {
				extraRoadAddr = ' (' + extraRoadAddr + ')';
			}

			document.getElementById('sample4_postcode').value = data.zonecode;
			document.getElementById("sample4_roadAddress").value = roadAddr;
			document.getElementById("sample4_jibunAddress").value = data.jibunAddress;

			if (roadAddr !== '') {
				document.getElementById("sample4_extraAddress").value = extraRoadAddr;
			} else {
				document.getElementById("sample4_extraAddress").value = '';
			}

			var guideTextBox = document.getElementById("guide");
			if (data.autoRoadAddress) {
				var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
				guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
				guideTextBox.style.display = 'block';

			} else if (data.autoJibunAddress) {
				var expJibunAddr = data.autoJibunAddress;
				guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
				guideTextBox.style.display = 'block';
			} else {
				guideTextBox.innerHTML = '';
				guideTextBox.style.display = 'none';
			}
		}
	}).open();
}
// 이미지 등록
function previewImage(event) {
	const file = event.target.files[0];

	if (file) {
		const fileType = file.type;
		const validTypes = ['image/png', 'image/jpeg'];

		if (validTypes.includes(fileType)) {
			const reader = new FileReader();
			reader.onload = function(e) {
				const img = document.getElementById('profileImg');
				img.src = e.target.result;
			}

			reader.readAsDataURL(file);
		} else {
			Swal.fire({
				icon: 'error',
				title: '파일 형식 오류',
				text: 'PNG 또는 JPG 파일만 등록 가능합니다.',
				confirmButtonText: '확인'
			});

			document.getElementById('profile_image').value = "";
		}
	}
}

// 비밀번호 숨기기 
$(document).ready(function() {
	$('#pwHidden').on('click', function() {
		$(this).hide();
		$('#pwShow').show();
		$('#new_password').attr('type', 'text');
	});

	$('#pwShow').on('click', function() {
		$(this).hide();
		$('#pwHidden').show();
		$('#new_password').attr('type', 'password');
	});
});

// 정보 수정 등록
const form = document.getElementById('myeditUpdateFrm');

form.addEventListener('submit', (e) => {
    e.preventDefault();  

    const memberNo = form.member_no.value;
    const roadAddress = form.sample4_roadAddress.value;
    const detailAddress = form.sample4_detailAddress.value;
    const newPw = form.new_password.value;
    const profileImg = form.profile_image.files[0]; 
    const csrfToken = document.getElementById('csrf_token').value;


    const formData = new FormData();
    formData.append('roadAddress', roadAddress);
    formData.append('detailAddress', detailAddress);
    formData.append('newPassword', newPw);
    formData.append('file', profileImg);  
    formData.append('member_no', memberNo); 

    fetch('/employee/member/myedit/' + memberNo, {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': csrfToken 
        },
        body: formData 
    })
    .then(response => response.json()) 
    .then(data => {
        if (data.res_code == '200') {
            Swal.fire({
                icon: 'success',
                title: '수정 성공',
                text: data.res_msg,
                confirmButtonText: '닫기'
            }).then((result) => {
                alert("성공");
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: '수정 실패',
                text: data.res_msg,
                confirmButtonText: '닫기'
            });
        }
    })
    .catch(error => {
        console.error('오류 발생:', error);
    });
});
