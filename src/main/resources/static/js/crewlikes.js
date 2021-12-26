		$(function(){
			// 좋아요 버튼 클릭시
			
			var userId = $("#userId").val();
			var crewId = $("#crewId").val();
			
			$("#rec_update").click(function(){
				$.ajax({
					url: "/likes/update",
	                type: "POST",
	                data: {
	                    "userId": userId,
	                    "crewId": crewId
	                },
	               	complete: function () {
				        recCount();
	                },
				})
			})
			
			// 크루 좋아요수
		    function recCount() {
				
				$.ajax({
					url: "/likes/count",
	                type: "POST",
	                data: {
	                    "crewId": crewId
	                },
	                success: function (res) {
	                	$(".rec_count").html(res.count);
	                },
				})
		    };
		    
		    recCount(); // 처음 시작했을 때 실행되도록 해당 함수 호출
		});