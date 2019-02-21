# YAMLの仕様に則り、1: ONE が 1: THREE で上書きされ、ONEはなかったことになる。パースエラーにはならない
enum e:
    1: ONE
    2: TWO
    1: THREE
