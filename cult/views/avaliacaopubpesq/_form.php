<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubpesq */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="avaliacaopubpesq-form">

    <?php $form = ActiveForm::begin(); ?>

    <?= $form->field($model, 'nota')->textInput() ?>

    <!--?= $form->field($model, 'idpubpesq')->textInput() ?-->

    <?php								  		 	//usa se o conjunto de classe que auxilia o controle do arrya, colocando o ""USE yii\helpers\ArrayHelper;"" e como vc vai precisa do models, "USE app\models\Fisioterapeuta"
    $aluno = \yii\helpers\ArrayHelper::map(			           			// utiliza em map para criar um vetor de fisioterapeuta, como 1° parametro a classe estatica que retorna o conjunto de objetos do tipo fisioterapeuta, em 2° parametro o valor html que vai ser exibido e o html armazenado (quando vc faz um dropDown vc tem o valor armazenado e o valor exibido), vc precisa dizer quais campos eu vou inserir, (id -> valor armazenado e nome-> valor exibido)
        \app\models\Publicacaopesq::find()->all(), 'id', 'nome');	 	// em find vc pode dizer que "quer alguns" com o metodo find()->where("nome"=>"carlos") e para TODOS, find()->all();..
    echo $form->field($model, 'idpubpesq')->widget(\kartik\select2\Select2::classname(),								//para os parametros aqui vc tem os mesmo na pagina do kartik "demos.krajee.com/widget-details/selec2
        [
            'data'=> $aluno,							//coloca o vetor criado aqui dentro, que sao os dados
            'options' => ['placeholder' => 'selecione uma publicação'], 			//e a informação que vai esta no inputBox antes de vc seleciona qualquer elemento //parametro que vc coloca coisas que deixa o controller mais organizado
            'pluginOptions'=>[
                'allowClear' => true,						        // para aparecer o x do lado para desmarcar a seleçaõ
            ]
        ])
    ?>

    <div class="form-group">
        <?= Html::submitButton('Save', ['class' => 'btn btn-success']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
