<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaopesq */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="publicacaopesq-form">

    <?php $form = ActiveForm::begin(); ?>

    <?= $form->field($model, 'nome')->textInput() ?>

    <?= $form->field($model, 'redesocial')->textInput() ?>

    <?= $form->field($model, 'endereco')->textInput() ?>

    <?= $form->field($model, 'contato')->textInput() ?>

    <?= $form->field($model, 'email')->textInput() ?>

    <?= $form->field($model, 'atvexercida')->textInput() ?>

    <?= $form->field($model, 'categoria')->textInput() ?>

    <?= $form->field($model, 'anoinicio')->textInput() ?>

    <?= $form->field($model, 'cnpj')->textInput() ?>

    <?= $form->field($model, 'representacao')->textInput() ?>

    <?= $form->field($model, 'recurso')->textInput() ?>

    <?= $form->field($model, 'aprovado')->textInput() ?>

    <?= $form->field($model, 'latitude')->textInput() ?>

    <?= $form->field($model, 'longitude')->textInput() ?>

    <?= $form->field($model, 'geo_gps')->textInput() ?>

    <!--?= $form->field($model, 'pesquisador')->textInput() ?-->


    <?php								  		 	//usa se o conjunto de classe que auxilia o controle do arrya, colocando o ""USE yii\helpers\ArrayHelper;"" e como vc vai precisa do models, "USE app\models\Fisioterapeuta"
    $aluno = \yii\helpers\ArrayHelper::map(			           			// utiliza em map para criar um vetor de fisioterapeuta, como 1° parametro a classe estatica que retorna o conjunto de objetos do tipo fisioterapeuta, em 2° parametro o valor html que vai ser exibido e o html armazenado (quando vc faz um dropDown vc tem o valor armazenado e o valor exibido), vc precisa dizer quais campos eu vou inserir, (id -> valor armazenado e nome-> valor exibido)
        \app\models\Pesquisador::find()->all(), 'id', 'nome');	 	// em find vc pode dizer que "quer alguns" com o metodo find()->where("nome"=>"carlos") e para TODOS, find()->all();..
    echo $form->field($model, 'pesquisador')->widget(\kartik\select2\Select2::classname(),								//para os parametros aqui vc tem os mesmo na pagina do kartik "demos.krajee.com/widget-details/selec2
        [
            'data'=> $aluno,							//coloca o vetor criado aqui dentro, que sao os dados
            'options' => ['placeholder' => 'selecione um pesquisador'], 			//e a informação que vai esta no inputBox antes de vc seleciona qualquer elemento //parametro que vc coloca coisas que deixa o controller mais organizado
            'pluginOptions'=>[
                'allowClear' => true,						        // para aparecer o x do lado para desmarcar a seleçaõ
            ]
        ])
    ?>


    <?= $form->field($model, 'img1')->textInput() ?>

    <?= $form->field($model, 'img2')->textInput() ?>

    <?= $form->field($model, 'img3')->textInput() ?>

    <?= $form->field($model, 'img4')->textInput() ?>

    <!--?= $form->field($model, 'campo1')->textInput() ?-->

    <!--?= $form->field($model, 'campo2')->textInput() ?-->

    <!--?= $form->field($model, 'campo3')->textInput() ?-->

    <!--?= $form->field($model, 'campo4')->textInput() ?-->

    <!--?= $form->field($model, 'campo5')->textInput() ?-->

    <div class="form-group">
        <?= Html::submitButton('Save', ['class' => 'btn btn-success']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
