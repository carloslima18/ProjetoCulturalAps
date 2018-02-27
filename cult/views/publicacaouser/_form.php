<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaouser */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="publicacaouser-form">

    <?php $form = ActiveForm::begin(); ?>

    <?= $form->field($model, 'nome')->textInput() ?>

    <?= $form->field($model, 'redesocial')->textInput() ?>

    <?= $form->field($model, 'endereco')->textInput() ?>

    <?= $form->field($model, 'contato')->textInput() ?>

    <?= $form->field($model, 'email')->textInput() ?>

    <?= $form->field($model, 'atvexercida')->textInput() ?>

    <?= $form->field($model, 'categoria')->textInput() ?>

    <?= $form->field($model, 'aprovado')->textInput() ?>

    <?= $form->field($model, 'latitude')->textInput() ?>

    <?= $form->field($model, 'longitude')->textInput() ?>

    <?= $form->field($model, 'geo_gps')->textInput() ?>

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
