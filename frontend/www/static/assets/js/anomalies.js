function get_anomalies(){
    let inurl = "api/people/anomaly"
    jQuery.ajax({
        type: "GET",
       // dataType: "json",
        url: inurl,
        success: function(data){
            //console.log(data)
            let tabbod = ""
            for(let i = 0;i<data.length;i++){
                let anomaly = data[i]
                tabbod += "<tr>"
                tabbod += "<td>"+anomaly.year+"/"+anomaly.month+"/"+anomaly.day+"</td>"
                tabbod += "<td>"+anomaly.employeeId+"</td>"
                tabbod += "<td>"+anomaly.comment+"</td>"
                tabbod += "</tr>"
            }
            $("#anomalies_tbody").html(tabbod)
            $("#anomalies_table").show()
        }
    })
}


get_anomalies()