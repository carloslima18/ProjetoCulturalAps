<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;
use yii\helpers\ArrayHelper;

/* @var $this yii\web\View */
/* @var $model app\models\AvaliacaopubpesqSearch */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="avaliacaopubpesq-search">

    <?php $form = ActiveForm::begin([
        'action' => ['index'],
        'method' => 'get',
    ]); ?>

    <!--?= $form->field($model, 'id') ?-->

    <?= $form->field($model, 'nota') ?>

    <?= $form->field($model, 'idpubpesq') ?>



    <div class="form-group">
        <?= Html::submitButton('Search', ['class' => 'btn btn-primary']) ?>
        <?= Html::resetButton('Reset', ['class' => 'btn btn-default']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
