<?php

use yii\helpers\Html;

/* @var $this yii\web\View */
/* @var $model app\models\Pesquisador */

$this->title = 'Alterar pesquisador: {nameAttribute}';
$this->params['breadcrumbs'][] = ['label' => 'Pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = ['label' => $model->id, 'url' => ['view', 'id' => $model->nome]];
$this->params['breadcrumbs'][] = 'Update';
?>
<div class="pesquisador-update">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
