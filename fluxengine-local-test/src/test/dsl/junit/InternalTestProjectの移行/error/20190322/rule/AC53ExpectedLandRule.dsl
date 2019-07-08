test 1 s1:
    2019/01/23 21:00:00:
        ExpectedLandEvent:
            categoryGroupId: 53001           # �J�e�S���O���[�vID
            ctgrLyrNo: 53002                 # �J�e�S���K�w�ԍ�
            sort: 53003                      # �J�e�S���\�[�g��
            adProjectSeq: 53004              # �L���Č�SEQ
            upperLimitThresholdRate: 53005   # ���臒l��
            lowerLimitThresholdRate: 53006   # ����臒l��
            landPredictionCost: 53007        # �J�e�S���ꗗ_�T�}��.���n�\���R�X�g-����
            ctgrBudgetGoalValue: 53008       # �J�e�S���ꗗ_�T�}��.�J�e�S���\�Z(fee��)�ڕW�l
        inspect:
            holderState.currentState == "over":