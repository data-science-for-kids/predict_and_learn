var pageTop = {
  							p1 : "From the information we gathered from your responses, we have figured -",
  							p2 : "This suggests that * are best able to predict whom you will befriend.",
 							p3 : "How can we use this information to predict your friendship in general?",
 							p4 : "For a new person whom you are trying to befriend you could check what his or her * ",
 							p5 : "For example, when trying to befriend",
 							p6 : "Similarly",
 							p7 : "To express this in numbers, we can write this as:",
 							p8 : "",
 							p9 : "",
 							p10: "What would result from these?",
 							p11: "What if only one of your preferred features exist in a new person you're trying to befriend?",
 							p12: "",
 							p13: "We can then assign points",
 							p14: "For example",
 							p15: "",
 							p16: "",
 							p17: "",
 							p18: "How many points will this person receive?",
 							p19: "",
 							p20: "",
 							p21: "Based on the number of points, if can decide if would befriend the person",
 							p22: "For example",
 							p23: "",
 							p24: "We are ready to build preditor of our own- a method which can suggest whether you would befriend a new person ",
 							p25: "Let us revisit some of your responses and see what the predictor would calculate. We will then compare it with the score you provided.",
 							p26: "",
 							p27: "Of the friends you had reviewed,",
 							p28: "This means the predictor is correct"

						};

		var inferences = {
		                    male:     "You prefer to befriend people who are male.<br>",
		                    female:   "You prefer to befriend people who are female.<br>",
		                    indoor:   "You prefer to befriend people who like indoor activities.<br>",
		                    outdoor:  "You prefer to befriend people who like outdoor activities.<br>",
		                    new_name: "You prefer to befriend people who have old names.<br>",
		                    old_name: "You prefer to befriend people who have new names.<br>"

		                 };

		var choices = {
		                    c1: "Gender",
		                    c2: "Activity",
		                    c3: "Names",

		                    c11: "Is the name new or old?",
		                    c12: "Is the hobby indoor or outdoor",
		                    c13: "Is the person male or female",

		                    c511: "old name",
		                    c512: "new name",
		                    c521: "boy",
		                    c522: "girl",
		                    c531: "likes indoor activities",
		                    c532: "likes outdoor activities",

		                    c81: "Is the person boy or a girl?",
		                    c82: "Is the name new or old?",
		                    c83: "Is the activity outdoor or indoor"
		                    //Verify all
		              };


        var pageMiddle = {
                            p4: "We notice that *",
                            p5: "We notice that *",
                            p6: "If ( New person *)<br>\t then <b>befriend the person<b><br>else<br>\t <b>do not befriend the person<b>",
                            p7: "If ( New person *)<br>\t then <b>befriend the person<b><br>else<br>\t do not befriend the person<b>",
                            p10: "*",
                            p11: "*"

                         };

        var pageEnd = {
                            p5: "We inferred before that you use these features to befriend a person",
                            p6: "These are the features which you do not prefer",
                            p14: "There are * matches",
                            p19: "Since the total is greater than 1, you will befriend"
                        };