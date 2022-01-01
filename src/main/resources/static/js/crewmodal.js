	
		/* 크루 멤버 확인 버튼 클릭*/
		$('#follower').click(function() {
			
			
			$('#followModal').modal(); //id가 "followModal"인 모달창을 열어준다. 
			$('.modal-title').text("크루 멤버"); //modal 의 header 부분에 "크루 멤버"라는 값을 넣어준다. 
			
		})
	
		/* 크루 팔로우 요청 확인 버튼 클릭*/
		$('#followRequest').click(function() {
			
			
			$('#followRequestModal').modal(); //id가 "followModal"인 모달창을 열어준다. 
			$('.modal-title').text("크루 팔로우 요청"); //modal 의 header 부분에 "크루 멤버"라는 값을 넣어준다. 
			
		})


		/* 크루 멤버 확인 모달에서 멤버 내보내기 버튼 클릭*/
		function deleteMember(memberId){
			
 			var memberId = $("#memberId").val(); 
			var crewId = $("#crewId").val();
			console.log(memberId);
			
			$.ajax({
				url: "/crew/deletemember",
                type: "POST",
                data: {
                	"crewId": crewId,
                    "memberId": memberId
                },
                complete: function () {
                	$(".followModal").load(location.href + " .followModal");
                	$("#memberCount").load(location.href + " #memberCount");
                },
			})
			
		}

		/* 크루 팔로우 요청 모달에서 멤버 승인하기 버튼 클릭*/
		function acceptMember(requestId){
			
			var crewId = $("#crewId").val();
			
			$.ajax({
				url: "/crew/follow",
                type: "POST",
                data: {
                	"requestId": requestId,
                    "crewId": crewId
                },
                complete: function (data) {
                	$(".followModal").load(location.href + " .followModal");
                	$("#memberCount").load(location.href + " #memberCount");
               		$(".followRequestModal").load(location.href + " .followRequestModal");
          //     		$("#reqNum").load(location.href + " #reqNum");
		//			$("#reqNum").html(data);
					$("#forCrewAdmin").load(location.href + " #forCrewAdmin");
                },
			})
			
		}
		
		
		
		/* 크루 팔로우 요청 모달에서 멤버 거절하기 버튼 클릭*/
		function rejectMember(requestId){
			
			var crewId = $("#crewId").val();
			
			$.ajax({
				url: "/crew/reject",
                type: "POST",
                data: {
                	"requestId": requestId,
                    "crewId": crewId
                },
                complete: function () {
               		$(".followRequestModal").load(location.href + " .followRequestModal");
               		$("#reqNum").load(location.href + " #reqNum");
                },
			})
			
		}