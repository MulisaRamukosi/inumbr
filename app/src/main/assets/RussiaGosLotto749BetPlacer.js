javascript:(() => {

    const addNumSet = async (numbers, betNums) => {
        return new Promise((resolve) => {
            betNums.forEach((num) => {
                numbers[num - 1].click();
            });

            const addBtnCheck = setInterval(() => {
                const buttons = document.getElementsByClassName('btn btn-outline-success btn-add mt-2 ml-2 text-uppercase');
                if(buttons !== undefined && buttons !== null && buttons.length > 0){
                    clearInterval(addBtnCheck);
                    buttons[0].click();
                    resolve();
                }
            }, 200);
        });
    };

    const betNumbers = (setPos, betNumsSet, numbers) => {
        if(setPos < betNumsSet.length){
            let betNums = betNumsSet[setPos];
            addNumSet(numbers, betNums).then(() => {
                betNumbers(++setPos, betNumsSet, numbers);
            });
        }
        else{
            window.history.back();
        }

    };

    const startNumberSelection = () => {

        const betNumsSet = %s;

        const manualPlayBlockCheck = setInterval(() => {
            const manualPlayBlock = document.getElementById('manual-play-block');
            if(manualPlayBlock !== undefined && manualPlayBlock !== null){
                clearInterval(manualPlayBlockCheck);

                const numberPickerBlock = manualPlayBlock.childNodes[6];
                const numbers = numberPickerBlock.childNodes;

                betNumbers(0, betNumsSet, numbers);
            }
        }, 200);
    };

    const startPlacingBets = () => {
            const SixBallSelectorCheck = setInterval(() => {
                const ballSelectionOptions = document.getElementsByClassName('col-6 px-1 px-md-1');
                if(ballSelectionOptions !== undefined && ballSelectionOptions !== null && ballSelectionOptions.length > 0){
                    clearInterval(SixBallSelectorCheck);

                    const sixBallOption = ballSelectionOptions[%d];
                    sixBallOption.click();

                    startNumberSelection();
                }
            }, 200);
    };

    const gosLotoCheck = setInterval(() => {
        const gosLotoR749 = document.getElementById('card-gosloto-russia-7-49');
        if(gosLotoR749 !== undefined && gosLotoR749 !== null){
            clearInterval(gosLotoCheck);
            gosLotoR749.click();
            startPlacingBets();
        }
    }, 200);

})()