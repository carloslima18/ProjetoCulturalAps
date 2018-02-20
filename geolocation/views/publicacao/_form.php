<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacao */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="publicacao-form">

    <?php $form = ActiveForm::begin(); ?>

    <?= $form->field($model, 'nome')->textarea(['rows' => 6]) ?>

    <?= $form->field($model, 'redesocial')->textarea(['rows' => 6]) ?>

    <?= $form->field($model, 'endereco')->textarea(['rows' => 6]) ?>

    <?= $form->field($model, 'contato')->textarea(['rows' => 6]) ?>

    <?= $form->field($model, 'atvexercida')->textarea(['rows' => 6]) ?>

    <?= $form->field($model, 'categoria')->textarea(['rows' => 6]) ?>

    <?= $form->field($model, 'latitude')->textInput() ?>

    <?= $form->field($model, 'longitude')->textInput() ?>

    <?= $form->field($model, 'geo_gps')->textInput() ?>

    <?= $form->field($model, 'img1')->textInput() ?>

    <?= $form->field($model, 'img2')->textInput() ?>

    <?= $form->field($model, 'img3')->textInput() ?>

    <?= $form->field($model, 'img4')->textInput() ?>

    <?= $form->field($model, 'fk_user')->textInput() ?>

    <div class="form-group">
        <?= Html::submitButton($model->isNewRecord ? 'Create' : 'Update', ['class' => $model->isNewRecord ? 'btn btn-success' : 'btn btn-primary']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
