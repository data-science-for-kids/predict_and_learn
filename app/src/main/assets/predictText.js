
        var pageTop = {
  							p1 : "From the information we gathered from your responses, we have figured -",
  							p2 : "This suggests that * are best able to predict whom you will befriend.",
 							p3 : "How can we use this information to predict your friendship in general?",
 							p4 : "For a new person whom you are trying to befriend, you could check what his/her",
 							p5 : "For example, when trying to befriend",
 							p6 : "Similarly",
 							p7 : "To express this in numbers, we can write this as:",
 							p8 : "",
 							p9 : "",
 							p10: "What would result from these?",
 							p11: "What if only one of your prefered features exist in a new person you're trying to befriend?",
 							p12: "",
 							p13: "We can then assign points",
 							p14: "For example",
 							p15: "",
 							p16: "How many points will this person receive?",
 							p17: "",
 							p18: "",
 							p19: "Based on the number of points, if can decide if would befriend the person",
 							p20: "For example",
 							p21: "",
 							p22: "We are ready to build preditor of our own- a method which can suggest whether you would befriend a new person ",
 							p23: "Let us revisit some of your responses and see what the predictor would calculate. We will then compare it with the score you provided.",
 							p24: "",
 							p25: "Of the friends you had reviewed,",
 							p26: "This means the predictor is correct"

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
		                    c13: "Is the person male or female"
		                    //Verify c13
		              };


        var pageMiddle = {
                            p4: "We notice that *",
                            p5: "We notice that *",
                            p6: "If ( New person *)<br>\t then befriend the person<br>else<br>\t do not befriend the person",
                            p7: "If ( New person *)<br>\t then befriend the person<br>else<br>\t do not befriend the person",
                            p10: "*",
                            p11: "*"

                         };

        var pageEnd = {
                            p5: "We infered before that you use these features to befrriend aa person",
                            p6: "These are the features which you donot prefer",
                            p14: "There are * matches"
                        };