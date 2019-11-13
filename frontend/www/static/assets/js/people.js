function hide_all_previous_results(){
    $(".employeerequestdiv").hide()
}

function get_employee_presences(){
    console.log("Getting Employee Results")
    hide_all_previous_results()
    $("#employee_presences_div").show()
    let year = $("#year").val()
    let month = $("#month").val()
    let employeeid = $("#employeeid").val()
    let inurl = "/api/people/employeepresences/"+employeeid+"/"+year+"/"+month
    console.log(inurl)
    jQuery.ajax({
        type: "GET",
       // dataType: "json",
        url: inurl,
        success: function(data){
            console.log(data)
            let dayPres = data.dayPresences
            let tabbody = ""
            for(let i =0; i< dayPres.length;i++){
                let dp = dayPres[i]
                //console.log(dp)
                for(let j = 0; j< dp.presences.length;j++){
                    p = dp.presences[j]
                    let datetime = new Date(0)
                    datetime.setUTCSeconds(p.timestamp)
                    tabbody += "<tr>"
                    tabbody += "<td>"+datetime.toLocaleString("es-ES")+"</td>"
                    tabbody += "<td>"+p.scannerId+"</td>"
                    tabbody += "<td>"+p.comment+"</td>"
                    tabbody += "</tr>"
                }
            }
            //console.log(tabbody)
            $("#employee_presences_tbody").html(tabbody)
        }
    })
}

function get_employee_effective_hours(){
    console.log("Getting Employee Effective Hours")
    hide_all_previous_results()
    $("#employee_hours_div").show()
    let year = $("#year").val()
    let month = $("#month").val()
    let employeeid = $("#employeeid").val()
    let inurl = "/api/people/employeehours/"+employeeid+"/"+year+"/"+month
    console.log(inurl)
    jQuery.ajax({
        type: "GET",
       // dataType: "json",
        url: inurl,
        success: function(data){
            console.log(data)
            let dayHours = data.effectiveHours
            let tabbody = ""
            for(let i =0; i< dayHours.length;i++){
                let eh = dayHours[i]
                //console.log(dp)
                tabbody += "<tr>"
                tabbody += "<td>"+eh.day+"/"+eh.month+"/"+eh.year+"</td>"
                tabbody += "<td>"+eh.hours.toFixed(2)+"</td>"
                // if(eh.anomalies){
                //     tabbody += "<td>"+eh.anomalies+"</td>"
                // }else{
                //     tabbody += "<td>"+eh.anomalies+"</td>"
                // }
                tabbody += "</tr>"
            }
            //console.log(tabbody)
            $("#employee_hours_tbody").html(tabbody)
        }
    })
}