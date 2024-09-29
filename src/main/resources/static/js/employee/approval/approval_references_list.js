document.addEventListener('DOMContentLoaded', function(){
	const memberRows = document.querySelectorAll('.approval_row');
	    window.functionTypes = [4,8]; 
	    console.log("현재 기능 타입: " + window.functionTypes);
	memberRows.forEach(row => {
		row.addEventListener('click',function(){
			const approvalNo = this.getAttribute('data-approval_no');
			const approvalType = this.getAttribute('data-type');
			console.log(approvalNo);
			console.log(approvalType);
		 if (approvalType === 'VACATION') {
                window.location.href = '/employee/approval/approval_references_vacation_detail/'+approvalNo;
                 if (window.functionTypes.includes(4)) {
                                     markApprovalAsRead(4, approvalNo); 
                                 }                
            } else {
                window.location.href = '/employee/approval/approval_references_detail/'+approvalNo;
                 if (window.functionTypes.includes(8)) {
                                     markApprovalAsRead(8, approvalNo); 
                                 }                       
            }
		
		});
	});
	

});