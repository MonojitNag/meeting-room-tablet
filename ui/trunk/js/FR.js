var FR = function(){
    var rooms = [
		{floor : 4, name : 'Panorama', number : 401, location : [978,354,118,82] },
		{floor : 4, name : 'Pilotti', number : 402, location : [1099,354,118,82] },
		{floor : 4, name : 'Vauhtimato', number : 403, location : [1220,354,118,82] },
		{floor : 4, name : 'Space Shot', number : 404, location : [979,184,178,129] },
		{floor : 4, name : 'Regatta', number : 405, location : [1220,56,118,85] },
		{floor : 4, name : 'Kino', number : 406, location : [1099,56,118,85] },
		{floor : 4, name : 'Vekkula', number : 407, location : [978,56,118,85] },
		{floor : 4, name : 'Metkula', number : 408, location : [776,56,199,85] },
		{floor : 4, name : 'Keittiö', number : 499, location : [10,186,142,130,10,316,480,120] }
	];

  var dummy_reservations = [
    new Reservation({name : 'Kino', number : 406, owner : 'Mats', starttime : '2010-12-03 13:00:00.000', endtime : '2010-12-03 15:30:00.000'}),
    new Reservation({name : 'Keittiö', number: 499, owner : 'Mats', starttime : '2010-12-03 11:00:00.000', endtime : '2010-12-03 16:00:00.000'}),
    new Reservation({name : 'Metkula', number : 408, owner : 'Mats', starttime : '2010-12-03 08:00:00.000', endtime : '2010-12-03 10:00:00.000'})
  ];

	var populate_rooms = function() {
		$('#room_template').tmpl( rooms )
			.appendTo( "#floormap_4" );
	};
	
    populate_rooms();
	
	return {
		init : function() {
			this.render_reservations();
		},
		render_reservations: function(){
			$.ajax({
				url: 'http://dyn-2-182.lan.futurice.org:8000/reservation/',
				context: document.body,
				success: function(data){
					for (var i = 0; i < data.length; i++) {
						$('#room_' + data[i]['number']).css('background-color', 'yellow');
					}
				},
				error: function(msg){
					for (var i = 0; i < dummy_reservations.length; i++) {
						$('#room_' + dummy_reservations[i]['number']).css('background-color', 'yellow');
					}
					log(msg);
				}
			});
		}
    };
}();

$().ready(function() {
	FR.init();
});
