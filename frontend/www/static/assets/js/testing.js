function generate_test_data(){
    let inurl = "api/people/test/generatetestdata"
    console.log("Generating test data")
    jQuery.ajax({
        type: "GET",
       // dataType: "json",
        url: inurl,
        success: function(data){
            //console.log(data)
            alert("Data generated for dates 2019-1-1 to 2019-1-30 for employeeIds 1001 to 1010")
        },
        error: function(data){
            alert("Request Error: "+data)
        }
      });
}



